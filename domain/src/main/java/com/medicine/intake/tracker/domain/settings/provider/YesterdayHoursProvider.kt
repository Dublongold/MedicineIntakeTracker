package com.medicine.intake.tracker.domain.settings.provider

import kotlinx.coroutines.flow.Flow

interface YesterdayHoursProvider {
    val yesterdayHours: Flow<Int>
}