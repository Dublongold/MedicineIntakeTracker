package com.medicine.intake.tracker.ui.main.home.dialog.customintake

import com.medicine.intake.tracker.domain.intake.IntakeMapper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

fun validateAddCustomIntakeTime(
    localDate: LocalDate,
    minDateTime: LocalDateTime?,
    maxDateTime: LocalDateTime,
    time: String,
): AddCustomIntakeTimeValidationResult {
    // 1. Verify if the time is in a valid format:
    val localTime = try {
        IntakeMapper.stringToLocalTime(time)
    } catch (_: DateTimeParseException) {
        null
    }
    if (localTime == null) {
        return AddCustomIntakeTimeValidationResult.BadCustomTimeTime.InvalidFormat
    } else {
        // 2. Verify if the time is within the range:
        val expectedLocalDateTime = localDate.atTime(
            localTime
        )
        val minDateTime = minDateTime ?: LocalDateTime.MIN
        if (expectedLocalDateTime !in minDateTime..maxDateTime) {
            return AddCustomIntakeTimeValidationResult.BadCustomTimeTime.OutOfRange
        }
        // 3. If the time is valid and within the range, return success:
        return AddCustomIntakeTimeValidationResult.Success
    }
}