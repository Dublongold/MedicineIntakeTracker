package com.medicine.intake.tracker.domain.settings

import com.medicine.intake.tracker.domain.settings.provider.HoursProvider
import com.medicine.intake.tracker.domain.settings.provider.LanguageProvider
import com.medicine.intake.tracker.domain.settings.provider.ThemeProvider
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface SettingsRepository : ThemeProvider, LanguageProvider, HoursProvider {

    suspend fun updateTheme(theme: Theme)
    suspend fun updateLanguage(language: Language)
    suspend fun updateYesterdayHours(hours: Int)
    suspend fun updateGracePeriodHours(hours: Int)
    companion object {
        val classesForBind = arrayOf(
            SettingsRepository::class,
            ThemeProvider::class,
            LanguageProvider::class,
            *HoursProvider.classesForBind
        )
    }
}