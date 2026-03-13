package com.medicine.intake.tracker.domain.settings

import com.medicine.intake.tracker.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository(
    initialTheme: Theme = SettingsDefaults.DefaultTheme,
    initialLanguage: Language = SettingsDefaults.DefaultLanguage,
    initialYesterdayHours: Int = SettingsDefaults.DEFAULT_YESTERDAY_HOURS,
    initialGracePeriodHours: Int = SettingsDefaults.DEFAULT_GRACE_PERIOD_HOURS
) : SettingsRepository {
    private val _theme = MutableStateFlow(initialTheme)
    override val theme = _theme.asStateFlow()

    private val _language = MutableStateFlow(initialLanguage)
    override val language = _language.asStateFlow()

    private val _yesterdayHours = MutableStateFlow(initialYesterdayHours)
    override val yesterdayHours = _yesterdayHours.asStateFlow()

    private val _gracePeriodHours = MutableStateFlow(initialGracePeriodHours)
    override val gracePeriodHours = _gracePeriodHours.asStateFlow()

    override suspend fun updateTheme(theme: Theme) {
        _theme.value = theme
    }

    override suspend fun updateLanguage(language: Language) {
        _language.value = language
    }

    override suspend fun updateYesterdayHours(hours: Int) {
        _yesterdayHours.value = hours
    }

    override suspend fun updateGracePeriodHours(hours: Int) {
        _gracePeriodHours.value = hours
    }

}