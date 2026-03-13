package com.medicine.intake.tracker.ui.main.home

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Loaded(
        val canTakeMedicine: Boolean,
        val recentIntakes: List<Intake>,
        val currentMedicineId: MedicineId?,
        val medicines: List<Medicine>,
        val gracePeriodHours: Int,
    ) : HomeUiState {
        val currentMedicine: Medicine?
            get() = medicines.find { it.id == currentMedicineId }
    }
}