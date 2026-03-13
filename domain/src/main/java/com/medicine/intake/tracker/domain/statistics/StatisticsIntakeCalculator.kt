package com.medicine.intake.tracker.domain.statistics

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import java.time.LocalDate
import java.time.LocalDateTime

class StatisticsIntakeCalculator(
    private val gracePeriodHours: Int
) {
    fun getLocalDate(intake: Intake): LocalDate {
        val hours = intake.time.takeWhile { it != IntakeMapper.TIME_SEPARATOR }.toInt()
        val gracedHours = hours - gracePeriodHours
        val date = IntakeMapper.stringToLocalDate(intake.date)
        return if (gracedHours >= 0) {
            date
        } else {
            date.minusDays(1)
        }
    }
    fun getDailyIntakeCounts(intakes: List<Intake>): Map<LocalDate, Int> {
        return intakes
            .map { getLocalDate(it) }
            .groupingBy { it }
            .eachCount()
    }

    fun convertIntakesToLocalDateTimes(intakes: List<Intake>): List<LocalDateTime> {
        return intakes.map {
            IntakeMapper.stringsToLocalDateTime(date = it.date, time = it.time)
        }.sorted()
    }
}