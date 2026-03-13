package com.medicine.intake.tracker.ui.main.history

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId

sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Loaded(
        val history: Map<String, List<Intake>>,
        val currentMedicineId: MedicineId?,
        val medicines: List<Medicine>,
    ) : HistoryUiState
}