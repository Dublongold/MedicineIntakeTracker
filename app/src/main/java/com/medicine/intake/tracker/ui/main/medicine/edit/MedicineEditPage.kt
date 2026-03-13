package com.medicine.intake.tracker.ui.main.medicine.edit

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.medicine.FakeMedicineRepository
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineIcon
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.ui.composable.LoadingPlaceholder
import com.medicine.intake.tracker.ui.main.medicine.DeleteMedicineDialog
import com.medicine.intake.tracker.ui.main.medicine.contentDescription
import com.medicine.intake.tracker.ui.main.medicine.painter
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MedicineEditPage(
    medicineId: MedicineId?,
    onConfirm: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MedicineEditViewModel = koinViewModel()
) {
    val state = run {
        if (medicineId == null) {
            MedicineEditUiState.Edit(null)
        } else {
            LaunchedEffect(medicineId, viewModel) {
                viewModel.loadMedicine(medicineId)
            }
            val state by viewModel.state.collectAsState()
            state
        }
    }
    val modifier = modifier
        .fillMaxSize()
    when (val state = state) {
        MedicineEditUiState.Loading -> {
            LoadingPlaceholder(modifier)
        }

        is MedicineEditUiState.Edit -> {
            val medicine = state.medicine
            var medicineToDelete by remember { mutableStateOf<Medicine?>(null) }

            var name by remember(medicine) { mutableStateOf(medicine?.name ?: "") }
            var badName by remember(medicine) { mutableStateOf(false) }
            var description by remember(medicine) { mutableStateOf(medicine?.description ?: "") }
            var intakesPerDay by remember(medicine) { mutableStateOf(medicine?.intakesPerDay) }
            var badIntakesPerDay by remember(medicine) { mutableStateOf(false) }
            var icon by remember(medicine) { mutableStateOf(medicine?.icon) }

            Box(modifier) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(LocalDimensions.current.pagePadding),
                    verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.defaultContentSpacing)
                ) {
                    TextField(
                        name,
                        { name = it; badName = false },
                        label = { Text(stringResource(R.string.medicine_edit_name_label)) },
                        isError = badName,
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = if (badName) {
                            {
                                Text(
                                    if (name.trim().isEmpty()) {
                                        stringResource(R.string.medicine_edit_name_cannot_be_empty)
                                    } else stringResource(R.string.medicine_edit_existing_name),
                                )
                            }
                        } else null)
                    TextField(
                        description,
                        { description = it },
                        label = { Text(stringResource(R.string.medicine_edit_description_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        intakesPerDay?.toString() ?: "",
                        { value ->
                            val result = value.toIntOrNull()?.also {
                                intakesPerDay = it.coerceIn(1, 12)
                            }
                            if (result == null && value.isEmpty()) {
                                intakesPerDay = null
                            }
                            badIntakesPerDay = false
                        },
                        supportingText = { Text(stringResource(R.string.medicine_edit_intakes_per_day_hint)) },
                        label = { Text(stringResource(R.string.medicine_edit_intakes_per_day_label)) },
                        isError = badIntakesPerDay,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.connectedItemsSpacing)
                    ) {
                        Text(
                            stringResource(R.string.medicine_edit_icon_label),
                            style = MaterialTheme.typography.titleMedium
                        )
                        FlowRow(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            MedicineIconOption(
                                icon = painterResource(id = R.drawable.ic_no_medicine),
                                contentDescription = "No icon",
                                selected = icon == null,
                                onClick = { icon = null })
                            VerticalDivider(Modifier.height(48.dp))
                            MedicineIcon.entries.forEach {
                                MedicineIconOption(
                                    icon = it.painter,
                                    contentDescription = it.contentDescription,
                                    selected = icon == it,
                                    onClick = { icon = it })
                            }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            val intakesPerDay = intakesPerDay
                            if (name.trim().isNotEmpty() && intakesPerDay != null) {
                                val onComplete: (MedicineActionResult) -> Unit = {
                                    when (it) {
                                        MedicineActionResult.SameName -> {
                                            badName = true
                                        }

                                        MedicineActionResult.Success -> {
                                            onConfirm()
                                        }
                                    }
                                }
                                val newMedicine = Medicine(
                                    id = medicine?.id ?: 0,
                                    name = name.trim(),
                                    description = description.trim().ifEmpty { null },
                                    intakesPerDay = intakesPerDay,
                                    icon = icon,
                                )
                                viewModel.editOrAddMedicine(newMedicine, onComplete = onComplete)
                            } else {
                                badName = name.trim().isEmpty()
                                badIntakesPerDay = intakesPerDay == null
                            }
                        },
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(
                            stringResource(
                                if (medicine != null) {
                                    R.string.action_edit
                                } else {
                                    R.string.action_create
                                }
                            )
                        )
                    }

                    if (medicine != null) {
                        Button(
                            {
                                medicineToDelete = medicine
                            },
                            Modifier
                                .fillMaxWidth()
                                .heightIn(min = 52.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                            )
                        ) {
                            Text(
                                stringResource(R.string.action_delete),
                            )
                        }
                    }
                }
            }
            medicineToDelete?.also { medicine ->
                DeleteMedicineDialog(medicine = medicine, onDelete = {
                    medicineToDelete = null
                    viewModel.deleteMedicine(medicine)
                    onDelete()
                }, onDismissRequest = { medicineToDelete = null })
            }
        }
    }
}

@Composable
fun MedicineIconOption(
    icon: Painter,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
    )
    IconButton(
        onClick,
        modifier = modifier.border(
            1.dp, borderColor, CircleShape
        ),
    ) {
        Icon(
            icon, contentDescription = contentDescription, modifier = Modifier.size(36.dp)
        )
    }
}

@Preview
@Composable
private fun MedicineEditPagePreview() {
    val context = LocalContext.current

    Scaffold { paddingValues: PaddingValues ->
        MedicineEditPage(
//            remember { createFakeMedicines().random() },
            null, onConfirm = {
                Toast.makeText(
                    context, "Created", Toast.LENGTH_SHORT
                ).show()
            }, onDelete = {
                Toast.makeText(
                    context, "Deleted $paddingValues", Toast.LENGTH_SHORT
                ).show()
            }, modifier = Modifier.padding(paddingValues), viewModel = viewModel {
                MedicineEditViewModel(FakeMedicineRepository.withFakeData())
            })
    }
}