package com.medicine.intake.tracker.domain.datetime

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds

class CurrentLocalDateTimeProvider {
    val localDateTime = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(1.seconds)
        }
    }

    val localDate = localDateTime.map { it.toLocalDate() }
    val localDateTimeAtStartOfDay = localDateTime.map { it.toLocalDate().atStartOfDay() }
    val localTime = localDateTime.map { it.toLocalTime() }
}
