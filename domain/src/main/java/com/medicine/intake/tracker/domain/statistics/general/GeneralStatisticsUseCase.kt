package com.medicine.intake.tracker.domain.statistics.general

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.statistics.StatisticsIntakeCalculator
import com.medicine.intake.tracker.domain.statistics.StatisticsMedicineCalculator

class GeneralStatisticsUseCase(
    private val intakes: List<Intake>,
    private val medicines: List<Medicine>,
    private val gracePeriodHours: Int,
    private val intakeCalculator: StatisticsIntakeCalculator = StatisticsIntakeCalculator(
        gracePeriodHours
    ),
    private val medicineCalculator: StatisticsMedicineCalculator = StatisticsMedicineCalculator(
        intakeCalculator
    ),
) {
    private fun <V : Comparable<V>> List<Pair<Medicine, V>>.getTop5WithSorting(): List<Pair<Medicine, V>> =
        sortedWith(compareByDescending<Pair<Medicine, V>> { it.second }.thenBy { it.first.name })
            .take(5)

    fun getTotalIntakesCount() = intakes.size
    fun medicinesWithIntakes() =
        intakes.groupBy { it.medicineId }.mapKeys { (key, _) -> medicines.first { it.id == key } }

    fun getMedicineWithBestStreak(): Pair<Medicine, Int>? =
        medicinesWithIntakes().mapValues { (key, value) ->
            medicineCalculator.getMedicineBestStreak(
                medicine = key,
                intakes = value,
            )
        }.maxByOrNull { it.value }?.toPair()

    fun getMedicineWithBestAdherence(): Pair<Medicine, Float>? =
        medicinesWithIntakes().mapValues { (medicine, intakes) ->
            medicineCalculator.getMedicineAdherence(
                medicine = medicine,
                intakes = intakes,
            )
        }.maxByOrNull { it.value }?.toPair()


    fun getTopMedicinesWithMostIntakes() =
        medicinesWithIntakes().mapValues { (_, value) -> value.size }.toList().getTop5WithSorting()

    fun getTopMedicinesWithBestStreak() = medicinesWithIntakes().mapValues { (key, value) ->
        medicineCalculator.getMedicineBestStreak(
            medicine = key,
            intakes = value,
        )
    }.toList().getTop5WithSorting()

    fun getTopMedicinesByAdherence() = medicinesWithIntakes().mapValues { (medicine, intakes) ->
        medicineCalculator.getMedicineAdherence(
            medicine = medicine,
            intakes = intakes,
        )
    }.toList().getTop5WithSorting()

    fun createState() = GeneralStatisticsState(
        totalIntakesCount = getTotalIntakesCount(),
        medicineWithBestStreak = getMedicineWithBestStreak(),
        medicineWithBestAdherence = getMedicineWithBestAdherence(),
        topMedicinesWithMostIntakes = getTopMedicinesWithMostIntakes(),
        topMedicinesWithBestStreak = getTopMedicinesWithBestStreak(),
        topMedicinesByAdherence = getTopMedicinesByAdherence(),
    )
}