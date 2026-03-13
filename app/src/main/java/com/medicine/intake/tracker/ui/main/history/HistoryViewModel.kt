package com.medicine.intake.tracker.ui.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicine.intake.tracker.domain.intake.IntakeProvider
import com.medicine.intake.tracker.domain.medicine.CurrentMedicineIdUpdater
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.MedicineProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    intakeProvider: IntakeProvider,
    medicineProvider: MedicineProvider,
    private val currentMedicineIdUpdater: CurrentMedicineIdUpdater
) : ViewModel() {

    val state = combine(
        intakeProvider.sortedIntakes,
        medicineProvider.currentMedicineId,
        medicineProvider.medicines,
    ) { history, currentMedicineId, medicines ->
        HistoryUiState.Loaded(
            history.filter {
                it.medicineId == currentMedicineId
            }.groupBy {
                it.date
            },
            currentMedicineId,
            medicines
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HistoryUiState.Loading
    )


    fun updateCurrentMedicineId(medicineId: MedicineId) {
        viewModelScope.launch {
            currentMedicineIdUpdater.updateCurrentMedicineId(medicineId)
        }
    }
}
