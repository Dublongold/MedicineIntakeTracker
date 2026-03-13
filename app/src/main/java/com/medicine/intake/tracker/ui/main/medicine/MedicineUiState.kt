package com.medicine.intake.tracker.ui.main.medicine

import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId

sealed interface MedicineUiState {
    object Loading : MedicineUiState
    data class Loaded(
        val selectedMedicineId: MedicineId?,
        val medicines: List<Medicine>,
    ) : MedicineUiState
}