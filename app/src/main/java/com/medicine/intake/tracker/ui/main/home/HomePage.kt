package com.medicine.intake.tracker.ui.main.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowSizeClass
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.datetime.CurrentLocalDateTimeProvider
import com.medicine.intake.tracker.domain.intake.FakeIntakeRepository
import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.domain.intake.usecase.CanIntakeUseCase
import com.medicine.intake.tracker.domain.intake.usecase.RecentIntakesUseCase
import com.medicine.intake.tracker.domain.medicine.FakeMedicineRepository
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.settings.FakeSettingsRepository
import com.medicine.intake.tracker.ui.composable.IntakeItem
import com.medicine.intake.tracker.ui.composable.LoadingPlaceholder
import com.medicine.intake.tracker.ui.composable.medicine.MedicineAwareContent
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onHistoryClicked: () -> Unit,
    onCreateNewMedicine: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    var intakeToDelete by remember { mutableStateOf<Intake?>(null) }
    var medicineWithIntakesLimit by remember { mutableStateOf<Pair<Medicine, Boolean>?>(null) }
    var showAddCustomIntakeDialog by remember { mutableStateOf(false) }

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    if (windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
            WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        )
    ) {
        HomePageContent(
            modifier = modifier,
            onAddIntake = {
                if (viewModel.addIntake(false) == false) {
                    medicineWithIntakesLimit = it to false
                }
            },
            onAddCustomIntake = {
                val isIntakeLimit = viewModel.isIntakesLimit()
                if (isIntakeLimit == true) {
                    medicineWithIntakesLimit = it to true
                } else if (isIntakeLimit == false) {
                    showAddCustomIntakeDialog = true
                }
            },
            onHistoryClicked = onHistoryClicked,
            onSelectCurrentMedicine = viewModel::selectCurrentMedicine,
            onCreateNewMedicine = onCreateNewMedicine,
            state = state,
            onDeleteClick = {
                intakeToDelete = it
            })
    } else {
        HomePageCompactHeightContent(
            modifier = modifier,
            onAddIntake = {
                if (viewModel.addIntake(false) == false) {
                    viewModel.addIntake(false)
                }
            },
            onAddCustomIntake = {
                val isIntakeLimit = viewModel.isIntakesLimit()
                if (isIntakeLimit == true) {
                    medicineWithIntakesLimit = it to true
                } else if (isIntakeLimit == false) {
                    showAddCustomIntakeDialog = true
                }
            },
            onHistoryClicked = onHistoryClicked,
            onSelectCurrentMedicine = viewModel::selectCurrentMedicine,
            onCreateNewMedicine = onCreateNewMedicine,
            state = state,
            onDeleteClick = {
                intakeToDelete = it
            })
    }

    // Диалог удаления
    intakeToDelete?.also { intake ->
        val title = stringResource(R.string.home_delete_intake_dialog_title)
        val text = stringResource(R.string.home_delete_intake_dialog_message)
        val delete = stringResource(R.string.action_delete)
        val cancel = stringResource(R.string.action_cancel)
        AlertDialog(
            onDismissRequest = { intakeToDelete = null },
            title = { Text(title) },
            text = { Text(text) },
            icon = {
                Icon(
                    painterResource(R.drawable.ic_delete),
                    null,
                    Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteIntake(intake)
                        intakeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(delete)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    intakeToDelete = null
                }) { Text(cancel) }
            })
    }
    if (showAddCustomIntakeDialog) {
        val lastIntake = (state as? HomeUiState.Loaded)?.recentIntakes?.lastOrNull()
        AddCustomIntakeDialog(
            minDateTime = lastIntake?.let {
                IntakeMapper.intakeToLocalDateTime(it).plusHours(2)
            },
            onAdd = { date, time ->
                viewModel.addCustomIntake(date, time)
                showAddCustomIntakeDialog = false
            },
            onDismissRequest = {
                showAddCustomIntakeDialog = false
            },
        )
    }
    medicineWithIntakesLimit?.also { (medicine, isForCustom) ->
        IntakesLimitDialog(
            medicineName = medicine.name,
            intakesPerDay = medicine.intakesPerDay,
            onConfirm = {
                if (isForCustom) {
                    showAddCustomIntakeDialog = true
                } else {
                    viewModel.addIntake(true)
                }
                medicineWithIntakesLimit = null
            },
            onDismissRequest = {
                medicineWithIntakesLimit = null
            })
    }
}

@Composable
private fun HomePageContent(
    modifier: Modifier,
    onCreateNewMedicine: () -> Unit,
    onAddIntake: (Medicine) -> Unit,
    onAddCustomIntake: (Medicine) -> Unit,
    onHistoryClicked: () -> Unit,
    onSelectCurrentMedicine: (MedicineId) -> Unit,
    state: HomeUiState,
    onDeleteClick: (Intake) -> Unit
) {
    if (state is HomeUiState.Loaded) {
        MedicineAwareContent(
            currentMedicineId = state.currentMedicineId,
            medicines = state.medicines,
            onCreateNewMedicine = onCreateNewMedicine,
            onMedicineSelected = onSelectCurrentMedicine,
            modifier = modifier,
            medicineUiPadding = PaddingValues(LocalDimensions.current.pagePadding),
            contentPadding = PaddingValues(
                top = LocalDimensions.current.defaultContentSpacing,
            )
        ) { content ->
            content(Modifier) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(
                            start = LocalDimensions.current.pagePadding,
                            end = LocalDimensions.current.pagePadding,
                            bottom = LocalDimensions.current.pagePadding
                        )
                ) {
                    val medicine = state.currentMedicine ?: return@content
                    RecentIntakesList(
                        medicineName = medicine.name,
                        intakes = state.recentIntakes,
                        onHistoryClicked = onHistoryClicked,
                        onDeleteClick = onDeleteClick,
                        listPadding = PaddingValues(
                            bottom = LocalDimensions.current.run {
                                pagePadding - listSpacing
                            }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    HorizontalDivider()
                    TakeMedicineButton(
                        medicineName = medicine.name,
                        onClick = {
                            onAddIntake(medicine)
                        },
                        onLongClick = {
                            onAddCustomIntake(medicine)
                        },
                        enabled = state.canTakeMedicine,
                        modifier = Modifier
                            .padding(top = LocalDimensions.current.pagePadding)
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f)
                    )
                }
            }
        }
//        if (state.medicines.isNotEmpty()) {
//            val medicine = state.currentMedicine
//            if (medicine != null) {
//                Column(
//                    modifier = modifier
//                        .fillMaxSize()
//                        .padding(
//                            start = LocalDimensions.current.pagePadding,
//                            end = LocalDimensions.current.pagePadding,
//                            bottom = LocalDimensions.current.pagePadding
//                        ),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    AnimatedVisibility(
//                        state.medicines.size > 1,
//                        enter = slideInVertically { -it } + expandVertically { -it },
//                        exit = slideOutVertically { -it } + shrinkVertically { -it },
//                    ) {
//                        MedicineDropdownMenu(
//                            selectedId = state.currentMedicineId,
//                            values = state.medicines,
//                            onSelected = onSelectCurrentMedicine,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(
//                                    bottom = LocalDimensions.current.listSpacing,
//                                    top = LocalDimensions.current.pagePadding,
//                                )
//                        )
//                    }
//                    AnimatedVisibility(
//                        state.medicines.size <= 1,
//                        enter = slideInVertically { -it } + expandVertically { -it },
//                        exit = slideOutVertically { -it } + shrinkVertically { -it },
//                    ) {
//                        Spacer(Modifier.height(LocalDimensions.current.pagePadding))
//                    }
//
//                }
//            } else {
//                MedicineNotSelectedContent(modifier)
//            }
//        } else {
//            NoMedicinesContent(
//                onCreateNewMedicine, modifier.padding(LocalDimensions.current.pagePadding)
//            )
//        }
    } else {
        LoadingPlaceholder()
    }
}

@Composable
private fun HomePageCompactHeightContent(
    modifier: Modifier,
    onCreateNewMedicine: () -> Unit,
    onAddIntake: (Medicine) -> Unit,
    onAddCustomIntake: (Medicine) -> Unit,
    onHistoryClicked: () -> Unit,
    onSelectCurrentMedicine: (MedicineId) -> Unit,
    state: HomeUiState,
    onDeleteClick: (Intake) -> Unit
) {
    if (state is HomeUiState.Loaded) {
        MedicineAwareContent(
            currentMedicineId = state.currentMedicineId,
            medicines = state.medicines,
            onCreateNewMedicine = onCreateNewMedicine,
            onMedicineSelected = onSelectCurrentMedicine,
            modifier = modifier,
            medicineUiPadding = PaddingValues(
                start = LocalDimensions.current.pagePadding,
                top = LocalDimensions.current.pagePadding,
                end = LocalDimensions.current.defaultContentSpacing,
                bottom = LocalDimensions.current.pagePadding,
            ),
            contentPadding = PaddingValues(
                top = LocalDimensions.current.defaultContentSpacing,
            )
        ) { content ->
            val medicine = state.currentMedicine ?: return@MedicineAwareContent
            Row {
                content(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    RecentIntakesList(
                        medicineName = medicine.name,
                        intakes = state.recentIntakes,
                        onHistoryClicked = onHistoryClicked,
                        onDeleteClick = onDeleteClick,
                        listPadding = LocalDimensions.current.run {
                            PaddingValues(bottom = pagePadding - listSpacing)
                        },
                        noIntakesModifier = Modifier.padding(bottom = LocalDimensions.current.pagePadding),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(
                                end = LocalDimensions.current.defaultContentSpacing,
                                start = LocalDimensions.current.pagePadding
                            )
                    )
                }
                TakeMedicineButton(
                    medicineName = medicine.name, onClick = {
                        onAddIntake(medicine)
                    }, onLongClick = {
                        onAddCustomIntake(medicine)
                    }, enabled = state.canTakeMedicine, modifier = Modifier
                        .padding(
                            bottom = LocalDimensions.current.pagePadding,
                            top = LocalDimensions.current.pagePadding,
                            end = LocalDimensions.current.pagePadding,
                        )
                        .fillMaxHeight()
                        .fillMaxWidth(0.4f)
                )
            }
        }
    } else {
        LoadingPlaceholder(modifier)
    }
}


@Composable
private fun TakeMedicineButton(
    medicineName: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonColors = ButtonDefaults.buttonColors()
    val contentColor by animateColorAsState(
        if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor
    )
    val containerColor by animateColorAsState(
        if (enabled) buttonColors.containerColor else buttonColors.disabledContainerColor
    )
    Box(
        modifier
            .background(containerColor, MaterialTheme.shapes.large)
            .clip(MaterialTheme.shapes.large)
            .combinedClickable(
                interactionSource = null,
                indication = ripple(color = contentColor),
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .padding(ButtonDefaults.ContentPadding),
        Alignment.Center,
    ) {
        Text(
            text = if (enabled) {
                stringResource(R.string.home_take_medicine, medicineName)
            } else stringResource(R.string.home_intake_unavailable),
            style = MaterialTheme.typography.titleMedium,
            color = contentColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RecentIntakesList(
    medicineName: String,
    intakes: List<Intake>?,
    onHistoryClicked: () -> Unit,
    onDeleteClick: (Intake) -> Unit,
    listPadding: PaddingValues,
    modifier: Modifier = Modifier,
    noIntakesModifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.home_recent_intakes),
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(
                onClick = onHistoryClicked,
                modifier = Modifier.padding(start = LocalDimensions.current.defaultContentSpacing)
            ) {
                Text(stringResource(R.string.home_full_history), textAlign = TextAlign.End)
            }
        }
        if (intakes.isNullOrEmpty()) {
            Box(
                noIntakesModifier
                    .fillMaxWidth()
                    .weight(1f), Alignment.Center
            ) {
                if (intakes == null) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        stringResource(R.string.home_no_recent_intakes_for, medicineName),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing),
                contentPadding = listPadding
            ) {
                item { Spacer(Modifier) }
                items(intakes) { intake ->
                    IntakeItem(
                        intake = intake, formatFullDate = true, onDeleteClick = {
                            onDeleteClick(intake)
                        })
                }
                item { Spacer(Modifier) }
            }
        }
    }
}

//@Preview
@Preview
@Preview(device = "spec:width=720px, height=1280px, dpi=320,orientation=landscape")
@Composable
private fun HomePagePreview() {
    Scaffold { paddingValues ->
        HomePage(
            viewModel = viewModel {
                val intakeRepository = FakeIntakeRepository.withFakeData()
                val medicineRepository = FakeMedicineRepository.withFakeData()
                val settingsRepository = FakeSettingsRepository()
                val currentLocalDateTimeProvider = CurrentLocalDateTimeProvider()
                HomeViewModel(
                    intakeDeleter = intakeRepository,
                    intakeWriter = intakeRepository,
                    canIntakeUseCase = CanIntakeUseCase(
                        currentLocalDateTimeProvider = currentLocalDateTimeProvider,
                        currentMedicineIdProvider = medicineRepository,
                        intakeProvider = intakeRepository
                    ),
                    recentIntakesUseCase = RecentIntakesUseCase(
                        currentLocalDateTimeProvider = currentLocalDateTimeProvider,
                        currentMedicineIdProvider = medicineRepository,
                        yesterdayHoursProvider = settingsRepository,
                        intakeProvider = intakeRepository
                    ),
                    currentMedicineIdUpdater = medicineRepository,
                    medicineProvider = medicineRepository,
                    gracePeriodHoursProvider = settingsRepository
                )
            },
            onHistoryClicked = {},
            onCreateNewMedicine = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}