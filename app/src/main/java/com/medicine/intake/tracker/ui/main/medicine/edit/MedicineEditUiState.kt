package com.medicine.intake.tracker.ui.main.medicine.edit

import com.medicine.intake.tracker.domain.medicine.Medicine

sealed interface MedicineEditUiState {
    object Loading : MedicineEditUiState
    data class Edit(
        val medicine: Medicine?,
    ) : MedicineEditUiState
}