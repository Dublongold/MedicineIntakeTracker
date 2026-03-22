package com.medicine.intake.tracker.domain.statistics

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.medicine.Medicine
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class StatisticsMedicineCalculator(private val intakeCalculator: StatisticsIntakeCalculator) {
    fun getMedicineBestStreak(
        medicine: Medicine,
        intakes: List<Intake>,
    ): Int {
        val dailyCounts =
            intakeCalculator.getDailyIntakeCounts(medicine.intakesPerDay, intakes).toSortedMap()

        var bestStreak = 0
        var currentStreak = 0
        var lastDate: LocalDate? = null

        for ((date, count) in dailyCounts) {
            if (count >= medicine.intakesPerDay) {
                if (lastDate == null || ChronoUnit.DAYS.between(lastDate, date) == 1L) {
                    currentStreak++
                } else {
                    bestStreak = maxOf(bestStreak, currentStreak)
                    currentStreak = 1
                }
                lastDate = date
            } else {
                bestStreak = maxOf(bestStreak, currentStreak)
                currentStreak = 0
            }
        }
        return maxOf(bestStreak, currentStreak)
    }

    fun getMedicineAdherence(
        medicine: Medicine,
        intakes: List<Intake>,
    ): Float {
        val allDatesWithIntakesCount = intakeCalculator.getDailyIntakeCounts(
            medicine.intakesPerDay,
            intakes = intakes
        )
        val allDates = allDatesWithIntakesCount.keys

        val validDates = allDatesWithIntakesCount
            .filter { it.value >= medicine.intakesPerDay }.keys

        if (validDates.isEmpty()) return 0f

        val minDate = allDates.minOrNull() ?: return 0f
        val maxDate = if (medicine.isCompleted) {
            allDates.maxOrNull() ?: return 0f
        } else {
            LocalDate.now()
        }

        val expectedDays = ChronoUnit.DAYS.between(minDate, maxDate).toFloat() + 1f
        val realDays = validDates.size.toFloat()

        return realDays / expectedDays * 100
    }

}