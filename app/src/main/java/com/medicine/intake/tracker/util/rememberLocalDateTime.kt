package com.medicine.intake.tracker.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.LocalDateTime

private fun LocalDateTime.atStartOfMinute(value: Boolean) = if (value) {
    this.withSecond(0).withNano(0)
} else this

/**
 * Provides a [LocalDateTime] that updates every minute (or second if [atStartOfMinute] is false).
 */
@Composable
fun rememberLocalDateTime(
    atStartOfMinute: Boolean = false
): LocalDateTime {
    var localDateTime by remember {
        mutableStateOf(LocalDateTime.now().atStartOfMinute(atStartOfMinute))
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            val now = System.currentTimeMillis()
            val timeToDelay = if (atStartOfMinute) {
                60_000 - (now % 60_000)
            } else {
                1_000 - (now % 1_000)
            }
            delay(timeToDelay)
            localDateTime = LocalDateTime.now().atStartOfMinute(atStartOfMinute)
        }
    }
    return localDateTime
}