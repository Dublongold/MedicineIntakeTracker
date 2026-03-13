package com.medicine.intake.tracker.ui.main.history.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicine.intake.tracker.domain.intake.IntakeProvider
import com.medicine.intake.tracker.domain.medicine.CurrentMedicineIdUpdater
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.MedicineProvider
import com.medicine.intake.tracker.domain.settings.provider.GracePeriodHoursProvider
import com.medicine.intake.tracker.domain.statistics.general.GeneralStatisticsUseCase
import com.medicine.intake.tracker.domain.statistics.medicine.MedicineStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StatisticsViewModel(
    intakeProvider: IntakeProvider,
    medicineProvider: MedicineProvider,
    private val currentMedicineIdUpdater: CurrentMedicineIdUpdater,
    gracePeriodHoursProvider: GracePeriodHoursProvider
) : ViewModel() {
    private val localCurrentMedicineId = MutableStateFlow<MedicineId?>(null)
    val state = combine(
        intakeProvider.sortedIntakes,
        medicineProvider.currentMedicineId,
        localCurrentMedicineId,
        medicineProvider.medicines,
        gracePeriodHoursProvider.gracePeriodHours,
    ) { intakes, currentMedicineId, localCurrentMedicineId, medicines, gracePeriodHours ->
        val medicineId = localCurrentMedicineId ?: currentMedicineId
        val generalUseCase = GeneralStatisticsUseCase(
            intakes = intakes,
            medicines = medicines,
            gracePeriodHours = gracePeriodHours
        )
        val medicineUseCase = if (medicines.isNotEmpty()) {
            MedicineStatisticsUseCase(
                currentMedicineId = medicineId,
                medicines = medicines,
                intakes = intakes,
                gracePeriodHours = gracePeriodHours,
            )
        } else null
        StatisticsUiState.Loaded(
            noMedicine = medicines.isEmpty(),
            medicines = medicines,
            currentMedicineId = medicineId,
            general = generalUseCase.createState(),
            medicine = medicineUseCase?.createState()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), StatisticsUiState.Loading)

    fun updateCurrentMedicineId(medicineId: MedicineId) {
        localCurrentMedicineId.value = medicineId
    }
}