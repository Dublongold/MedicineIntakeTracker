package com.medicine.intake.tracker.domain.intake

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object IntakeMapper {
    const val TIME_SEPARATOR = ':'
    const val DATE_SEPARATOR = '-'
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy${DATE_SEPARATOR}MM${DATE_SEPARATOR}dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH${TIME_SEPARATOR}mm")
    fun intakeToLocalDateTime(intake: Intake): LocalDateTime = stringsToLocalDateTime(
        intake.date, intake.time
    )

    fun localDateToString(localDate: LocalDate): String = localDate.format(dateFormatter)

    fun localTimeToString(localTime: LocalTime): String = localTime.format(timeFormatter)

    fun stringToLocalDate(date: String): LocalDate = LocalDate.parse(date, dateFormatter)
    fun timeUnitsToString(hours: Int, minutes: Int): String = "%02d:%02d".format(hours, minutes)
    /**
     * @throws[java.time.format.DateTimeParseException]
     */
    fun stringToLocalTime(time: String): LocalTime = LocalTime.parse(time, timeFormatter)
    fun stringsToLocalDateTime(date: String, time: String): LocalDateTime =
        LocalDateTime.of(stringToLocalDate(date), stringToLocalTime(time))
    fun localDateTimeToStrings(localDateTime: LocalDateTime): Pair<String, String> =
        Pair(localDateToString(localDateTime.toLocalDate()), localTimeToString(localDateTime.toLocalTime()))
}