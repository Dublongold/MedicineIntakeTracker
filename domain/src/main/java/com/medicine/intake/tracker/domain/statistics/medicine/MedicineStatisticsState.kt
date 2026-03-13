package com.medicine.intake.tracker.domain.statistics.medicine

import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import java.time.Duration

data class MedicineStatisticsState(
    val currentMedicineId: MedicineId,
    val medicines: List<Medicine>,
    val medicineTotalIntakesCount: Int,
    val adherence: Float,
    val bestStreak: Int,
    val averageIntakesPerDay: Float,
    val shortestIntervalBetweenIntakes: Duration,
    val longestIntervalBetweenIntakes: Duration,
    val averageIntervalBetweenIntakes: Duration,
)