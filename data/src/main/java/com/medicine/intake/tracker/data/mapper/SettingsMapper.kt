package com.medicine.intake.tracker.data.mapper

import com.medicine.intake.tracker.domain.settings.Language
import com.medicine.intake.tracker.domain.settings.Theme

object SettingsMapper {
    fun themeToString(theme: Theme): String = theme.name
    fun stringToTheme(theme: String): Theme = Theme.valueOf(theme)

    fun stringToLanguage(language: String): Language = Language.valueOf(language)
    fun languageToString(language: Language): String = language.name
}