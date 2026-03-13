package com.medicine.intake.tracker.ui.main.settings

import com.medicine.intake.tracker.domain.settings.Language
import com.medicine.intake.tracker.domain.settings.Theme

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Loaded(
        val theme: Theme,
        val language: Language,
        val yesterdayHours: Int,
        val gracePeriodHours: Int,
    ) : SettingsUiState
}