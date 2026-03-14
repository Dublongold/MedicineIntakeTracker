package com.medicine.intake.tracker.domain.statistics.medicine

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.statistics.StatisticsIntakeCalculator
import com.medicine.intake.tracker.domain.statistics.StatisticsMedicineCalculator
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

class MedicineStatisticsUseCase(
    currentMedicineId: MedicineId?,
    private val medicines: List<Medicine>,
    private val intakes: List<Intake>,
    private val gracePeriodHours: Int,
    private val intakeCalculator: StatisticsIntakeCalculator = StatisticsIntakeCalculator(
        gracePeriodHours
    ),
    private val medicineCalculator: StatisticsMedicineCalculator = StatisticsMedicineCalculator(
        intakeCalculator
    ),
) {
    private val currentMedicineId: MedicineId = currentMedicineId ?: medicines.first().id
    fun medicineIntakes(): List<Intake> = intakes.filter { it.medicineId == currentMedicineId }
    fun currentMedicineWithIntakes(): Pair<Medicine, List<Intake>> {
        val currentMedicine = medicines.first { it.id == currentMedicineId }
        return currentMedicine to medicineIntakes()
    }

    fun getMedicineTotalIntakesCount() = medicineIntakes().size
    fun getMedicineAdherence() = currentMedicineWithIntakes().let { (medicine, intakes) ->
        medicineCalculator.getMedicineAdherence(
            medicine = medicine,
            intakes = intakes
        )
    }

    fun getMedicineBestStreak() = currentMedicineWithIntakes().let { (medicine, intakes) ->
        medicineCalculator.getMedicineBestStreak(
            medicine = medicine,
            intakes = intakes
        )
    }

    fun getMedicineAverageIntakesPerDay() = intakeCalculator.getDailyIntakeCounts(
        medicines.first { it.id == currentMedicineId }.intakesPerDay,
        medicineIntakes(),
    )
        .values.average().toFloat().let {
            if (it.isNaN()) 0f else it
        }

    fun getMedicineShortestIntervalBetweenIntakes(): Duration {
        var currentInterval = Duration.ZERO
        val intakesLocalDateTimes = intakeCalculator.convertIntakesToLocalDateTimes(
            medicineIntakes()
        )
        var lastLocalDateTime: LocalDateTime? = null
        for (localDateTime in intakesLocalDateTimes) {
            if (lastLocalDateTime != null) {
                val interval = Duration.between(lastLocalDateTime, localDateTime)
                if (currentInterval == Duration.ZERO || interval < currentInterval) {
                    currentInterval = interval
                }
            }
            lastLocalDateTime = localDateTime
        }
        return currentInterval
    }

    fun getMedicineLongestIntervalBetweenIntakes(): Duration {
        var currentInterval = Duration.ZERO
        val intakesLocalDateTimes = intakeCalculator.convertIntakesToLocalDateTimes(
            medicineIntakes()
        )
        var lastLocalDateTime: LocalDateTime? = null
        for (localDateTime in intakesLocalDateTimes) {
            if (lastLocalDateTime != null) {
                val interval = Duration.between(lastLocalDateTime, localDateTime)
                if (interval > currentInterval) {
                    currentInterval = interval
                }
            }
            lastLocalDateTime = localDateTime
        }
        return currentInterval
    }

    fun getMedicineAverageIntervalBetweenIntakes(): Duration {
        val intakesLocalDateTimes = medicineIntakes().map {
            IntakeMapper.stringsToLocalDateTime(date = it.date, time = it.time)
        }.sorted()
        if (intakesLocalDateTimes.size < 2) {
            return Duration.ZERO
        }
        val intervals = mutableListOf<Long>()
        var lastLocalDateTime: LocalDateTime? = null
        for (localDateTime in intakesLocalDateTimes) {
            if (lastLocalDateTime != null) {
                intervals += ChronoUnit.MINUTES.between(lastLocalDateTime, localDateTime)
            }
            lastLocalDateTime = localDateTime
        }
        val average = intervals
            .average()
            .toDuration(DurationUnit.MINUTES)
            .toJavaDuration()

        return average
    }

    fun createState() = MedicineStatisticsState(
        currentMedicineId = currentMedicineId,
        medicines = medicines,
        medicineTotalIntakesCount = getMedicineTotalIntakesCount(),
        adherence = getMedicineAdherence(),
        bestStreak = getMedicineBestStreak(),
        averageIntakesPerDay = getMedicineAverageIntakesPerDay(),
        shortestIntervalBetweenIntakes = getMedicineShortestIntervalBetweenIntakes(),
        longestIntervalBetweenIntakes = getMedicineLongestIntervalBetweenIntakes(),
        averageIntervalBetweenIntakes = getMedicineAverageIntervalBetweenIntakes()

    )
}