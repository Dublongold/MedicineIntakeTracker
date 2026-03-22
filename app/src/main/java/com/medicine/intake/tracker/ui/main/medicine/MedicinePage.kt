package com.medicine.intake.tracker.ui.main.medicine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.medicine.FakeMedicineRepository
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.ui.composable.LoadingPlaceholder
import com.medicine.intake.tracker.ui.main.medicine.dialog.DeleteMedicineDialog
import com.medicine.intake.tracker.ui.main.medicine.dialog.MedicineDialog
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MedicinePage(
    onEdit: (MedicineId?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MedicineViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val medicines = (state as? MedicineUiState.Loaded)?.medicines ?: emptyList()
    var idOfCurrentMedicineDetails: MedicineId? by remember { mutableStateOf(null) }
    var idOfMedicineToDelete: MedicineId? by remember { mutableStateOf(null) }
    when (val state = state) {
        is MedicineUiState.Loaded -> {
            Box(modifier.fillMaxSize(), Alignment.Center) {
                if (state.medicines.isEmpty()) {
                    Text(
                        stringResource(R.string.medicines_no_medicines),
                        Modifier.padding(LocalDimensions.current.pagePadding),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            LocalDimensions.current.listSpacing
                        ),
                        contentPadding = PaddingValues(
                            start = LocalDimensions.current.pagePadding,
                            top = LocalDimensions.current.pagePadding,
                            end = LocalDimensions.current.pagePadding,
                            bottom = 56.dp + LocalDimensions.current.pagePadding * 2,
                        )
                    ) {
                        items(state.medicines) { medicine ->
                            MedicineCard(medicine = medicine, onClick = {
                                if (state.selectedMedicineId != medicine.id) {
                                    viewModel.updateCurrentMedicineId(medicine.id)
                                } else {
                                    idOfCurrentMedicineDetails = medicine.id
                                }
                            }, onDeleteClick = {
                                idOfMedicineToDelete = medicine.id
                            }, highlight = state.selectedMedicineId == medicine.id)
                        }
                    }
                }
//                // TODO: Remove this button on release.
//                FloatingActionButton(
//                    viewModel::debugAddRandomMedicine, Modifier
//                        .align(Alignment.BottomStart)
//                        .padding(LocalDimensions.current.pagePadding)
//                        .size(56.dp)
//                ) {
//                    Text("R")
//                }
                FloatingActionButton(
                    {
                        onEdit(null)
                    }, Modifier
                        .align(Alignment.BottomEnd)
                        .padding(LocalDimensions.current.pagePadding)
                        .size(56.dp)
                ) {
                    Text("+")
                }
            }
        }

        MedicineUiState.Loading -> {
            LoadingPlaceholder(modifier)
        }
    }
    val currentMedicineDetails = medicines.find { idOfCurrentMedicineDetails == it.id }
    currentMedicineDetails?.also { medicine ->
        MedicineDialog(
            medicine = medicine,
            onDismissRequest = { idOfCurrentMedicineDetails = null },
            onCompletedChanged = { isCompleted ->
                viewModel.updateMedicineCompleted(medicine, isCompleted)
            },
            onEditClick = {
                onEdit(medicine.id)
                idOfCurrentMedicineDetails = null
            })
    }
    val medicineToDelete = medicines.find { idOfMedicineToDelete == it.id }
    medicineToDelete?.also { medicine ->
        DeleteMedicineDialog(
            medicine = medicine,
            onDelete = {
                viewModel.deleteMedicine(medicine)
                idOfMedicineToDelete = null
            },
            onDismissRequest = {
                idOfMedicineToDelete = null
            }
        )
    }
}

@Preview
@Composable
private fun MedicinePagePreview() {
    Scaffold {
        MedicinePage(onEdit = {}, modifier = Modifier.padding(it), viewModel = viewModel {
            val medicineRepository = FakeMedicineRepository.withFakeData()
            MedicineViewModel(
                medicineProvider = medicineRepository,
                currentMedicineIdUpdater = medicineRepository,
                medicineDeleter = medicineRepository,
                medicineWriter = medicineRepository,
            )
        })
    }
}