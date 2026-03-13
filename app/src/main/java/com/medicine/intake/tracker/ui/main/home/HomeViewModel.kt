package com.medicine.intake.tracker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.IntakeDeleter
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.domain.intake.IntakeWriter
import com.medicine.intake.tracker.domain.intake.usecase.AddIntakeUseCase
import com.medicine.intake.tracker.domain.intake.usecase.CanIntakeUseCase
import com.medicine.intake.tracker.domain.intake.usecase.RecentIntakesUseCase
import com.medicine.intake.tracker.domain.medicine.CurrentMedicineIdUpdater
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.MedicineProvider
import com.medicine.intake.tracker.domain.settings.provider.GracePeriodHoursProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

private const val TAG = "HomeViewModel"

private val addIntakeUseCase = AddIntakeUseCase()
class HomeViewModel(
    private val intakeDeleter: IntakeDeleter,
    private val intakeWriter: IntakeWriter,
    canIntakeUseCase: CanIntakeUseCase,
    recentIntakesUseCase: RecentIntakesUseCase,
    private val currentMedicineIdUpdater: CurrentMedicineIdUpdater,
    medicineProvider: MedicineProvider,
    gracePeriodHoursProvider: GracePeriodHoursProvider,
) : ViewModel() {

    val state = combine(
        canIntakeUseCase.canIntake,
        recentIntakesUseCase.recentIntakes,
        medicineProvider.currentMedicineId,
        medicineProvider.medicines,
        gracePeriodHoursProvider.gracePeriodHours
    ) { canIntake, recentIntakes, currentMedicineId, medicines, gracePeriodHours ->
        HomeUiState.Loaded(canIntake, recentIntakes, currentMedicineId, medicines, gracePeriodHours)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        HomeUiState.Loading
    )

    fun selectCurrentMedicine(medicineId: MedicineId) {
        viewModelScope.launch {
            currentMedicineIdUpdater.updateCurrentMedicineId(medicineId)
        }
    }

    fun addIntake(ignoreLimit: Boolean): Boolean? {
        val state = (state.value as? HomeUiState.Loaded) ?: return null
        val currentMedicine = state.currentMedicine ?: return null
        return addIntakeUseCase.addIntake(
            ignoreLimit = ignoreLimit,
            currentMedicine = currentMedicine,
            recentIntakes = state.recentIntakes,
            gracePeriodHours = state.gracePeriodHours,
            onAdd = {
                viewModelScope.launch {
                    val localDateTimeNow = LocalDateTime.now()
                    intakeWriter.addIntake(
                        Intake(
                            localDateTime = localDateTimeNow,
                            medicineId = currentMedicine.id
                        )
                    )
                }
            }
        )
    }

    fun isIntakesLimit(): Boolean? {
        val state = (state.value as? HomeUiState.Loaded) ?: return null
        val currentMedicine = state.currentMedicine ?: return null
        return addIntakeUseCase.isIntakesLimit(
            currentMedicine = currentMedicine,
            recentIntakes = state.recentIntakes,
            gracePeriodHours = state.gracePeriodHours
        )
    }

    fun addCustomIntake(date: String, time: String) {
        val state = (state.value as? HomeUiState.Loaded) ?: return
        val currentMedicine = state.currentMedicine ?: return
        viewModelScope.launch {
            intakeWriter.addIntake(
                Intake(
                    date = date,
                    time = time,
                    medicineId = currentMedicine.id
                )
            )
        }
    }

    fun deleteIntake(intake: Intake) {
        viewModelScope.launch {
            intakeDeleter.deleteIntake(intake)
        }
    }
}