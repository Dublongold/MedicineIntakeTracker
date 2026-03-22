package com.medicine.intake.tracker.ui.main.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowSizeClass
import com.medicine.intake.tracker.domain.datetime.CurrentLocalDateTimeProvider
import com.medicine.intake.tracker.domain.intake.FakeIntakeRepository
import com.medicine.intake.tracker.domain.intake.IntakeId
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.domain.intake.usecase.CanIntakeUseCase
import com.medicine.intake.tracker.domain.intake.usecase.RecentIntakesUseCase
import com.medicine.intake.tracker.domain.medicine.FakeMedicineRepository
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.settings.FakeSettingsRepository
import com.medicine.intake.tracker.ui.main.home.dialog.customintake.AddCustomIntakeDialog
import com.medicine.intake.tracker.ui.main.home.dialog.customintake.IntakesLimitDialog
import com.medicine.intake.tracker.ui.main.home.dialog.delete.DeleteIntakeDialog
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

    var intakeToDeleteId by remember { mutableStateOf<IntakeId?>(null) }
    var medicineIdWithIntakesLimit by remember { mutableStateOf<Pair<MedicineId, Boolean>?>(null) }
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
                    medicineIdWithIntakesLimit = it.id to false
                }
            },
            onAddCustomIntake = {
                val isIntakeLimit = viewModel.isIntakesLimit()
                if (isIntakeLimit == true) {
                    medicineIdWithIntakesLimit = it.id to true
                } else if (isIntakeLimit == false) {
                    showAddCustomIntakeDialog = true
                }
            },
            onHistoryClicked = onHistoryClicked,
            onSelectCurrentMedicine = viewModel::selectCurrentMedicine,
            onCreateNewMedicine = onCreateNewMedicine,
            state = state,
            onDeleteClick = {
                intakeToDeleteId = it.id
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
                    medicineIdWithIntakesLimit = it.id to true
                } else if (isIntakeLimit == false) {
                    showAddCustomIntakeDialog = true
                }
            },
            onHistoryClicked = onHistoryClicked,
            onSelectCurrentMedicine = viewModel::selectCurrentMedicine,
            onCreateNewMedicine = onCreateNewMedicine,
            state = state,
            onDeleteClick = {
                intakeToDeleteId = it.id
            })
    }

    val intakeToDelete = (state as? HomeUiState.Loaded)?.recentIntakes?.find { it.id == intakeToDeleteId }
    val currentMedicine = (state as? HomeUiState.Loaded)?.currentMedicine
    if (intakeToDelete != null && currentMedicine != null) {
        DeleteIntakeDialog(
            medicineName = currentMedicine.name,
            date = intakeToDelete.date,
            time = intakeToDelete.time,
            onDismissRequest = {
                intakeToDeleteId = null
            },
            onConfirm = {
                viewModel.deleteIntake(intakeToDelete)
                intakeToDeleteId = null
            },
        )
    }
    if (showAddCustomIntakeDialog) {
        val newestIntake = (state as? HomeUiState.Loaded)?.recentIntakes?.maxByOrNull {
            it.date + it.time
        }
        AddCustomIntakeDialog(
            minDateTime = newestIntake?.let {
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
    val medicineWithIntakesLimit = medicineIdWithIntakesLimit?.let { medicineIdWithIntakesLimit ->
        val medicineId = medicineIdWithIntakesLimit.first
        val medicine = (state as? HomeUiState.Loaded)?.medicines?.find { it.id == medicineId }
        medicine?.to(medicineIdWithIntakesLimit.second)
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
                medicineIdWithIntakesLimit = null
            },
            onDismissRequest = {
                medicineIdWithIntakesLimit = null
            })
    }
}

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