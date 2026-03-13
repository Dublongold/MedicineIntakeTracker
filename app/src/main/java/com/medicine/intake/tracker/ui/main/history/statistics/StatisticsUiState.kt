package com.medicine.intake.tracker.ui.main.history.statistics

import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.statistics.general.GeneralStatisticsState
import com.medicine.intake.tracker.domain.statistics.medicine.MedicineStatisticsState

sealed interface StatisticsUiState {
    object Loading : StatisticsUiState
    data class Loaded(
        val noMedicine: Boolean,
        val medicines: List<Medicine>,
        val currentMedicineId: MedicineId?,
        val general: GeneralStatisticsState,
        val medicine: MedicineStatisticsState?,
    ) : StatisticsUiState
}