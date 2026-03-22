package com.medicine.intake.tracker.ui.composable.dialog

import com.medicine.intake.tracker.domain.intake.IntakeMapper
import java.time.LocalTime

fun validateTimeSelectionTime(
    hour: Int,
    minute: Int,
    min: LocalTime?,
    max: LocalTime?,
): String? {
    val time = IntakeMapper.timeUnitsToString(
        hour,
        minute
    )
    val localTime = IntakeMapper.stringToLocalTime(time)

    val min = min ?: LocalTime.MIN
    val max = max ?: LocalTime.MAX

    return if (localTime in min..max) time else null
}