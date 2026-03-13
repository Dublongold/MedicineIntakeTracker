package com.medicine.intake.tracker.domain.statistics.general

import com.medicine.intake.tracker.domain.medicine.Medicine

data class GeneralStatisticsState(
    val totalIntakesCount: Int,
    val medicineWithBestStreak: Pair<Medicine, Int>?,
    val medicineWithBestAdherence: Pair<Medicine, Float>?,
    val topMedicinesWithMostIntakes: List<Pair<Medicine, Int>>,
    val topMedicinesWithBestStreak: List<Pair<Medicine, Int>>,
    val topMedicinesByAdherence: List<Pair<Medicine, Float>>,
)