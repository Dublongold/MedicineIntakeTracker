package com.medicine.intake.tracker.domain.settings.provider

import kotlinx.coroutines.flow.Flow

interface HoursProvider : YesterdayHoursProvider, GracePeriodHoursProvider {
    companion object {
        val classesForBind = arrayOf(
            HoursProvider::class,
            YesterdayHoursProvider::class,
            GracePeriodHoursProvider::class
        )
    }
}