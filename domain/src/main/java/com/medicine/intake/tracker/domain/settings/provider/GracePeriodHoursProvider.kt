package com.medicine.intake.tracker.domain.settings.provider

import kotlinx.coroutines.flow.Flow

interface GracePeriodHoursProvider {
    val gracePeriodHours: Flow<Int>
}