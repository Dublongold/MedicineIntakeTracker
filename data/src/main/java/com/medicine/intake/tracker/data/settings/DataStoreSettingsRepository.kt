package com.medicine.intake.tracker.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.medicine.intake.tracker.data.mapper.SettingsMapper
import com.medicine.intake.tracker.domain.settings.Language
import com.medicine.intake.tracker.domain.settings.SettingsDefaults
import com.medicine.intake.tracker.domain.settings.SettingsRepository
import com.medicine.intake.tracker.domain.settings.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.text.set

private val Context.dataStore by preferencesDataStore("settings")

private val ThemeKey = stringPreferencesKey("theme")
private val LanguageKey = stringPreferencesKey("language")
private val YesterdayHoursKey = intPreferencesKey("yesterday-hours")
private val GracePeriodHoursKey = intPreferencesKey("grace-period-hours")

class DataStoreSettingsRepository(context: Context) : SettingsRepository {
    private val dataStore = context.dataStore

    override val theme = dataStore.data.map {
        it[ThemeKey]?.let { themeConfigString ->
            SettingsMapper.stringToTheme(themeConfigString)
        } ?: SettingsDefaults.DefaultTheme
    }

    override val language: Flow<Language> = dataStore.data.map {
        it[LanguageKey]?.let { languageString ->
            SettingsMapper.stringToLanguage(languageString)
        } ?: SettingsDefaults.DefaultLanguage
    }

    override val yesterdayHours = dataStore.data.map {
        it[YesterdayHoursKey] ?: SettingsDefaults.DEFAULT_YESTERDAY_HOURS
    }

    override val gracePeriodHours: Flow<Int> = dataStore.data.map {
        it[GracePeriodHoursKey] ?: SettingsDefaults.DEFAULT_GRACE_PERIOD_HOURS
    }

    override suspend fun updateTheme(theme: Theme) {
        dataStore.edit {
            it[ThemeKey] = SettingsMapper.themeToString(theme)
        }
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit {
            it[LanguageKey] = SettingsMapper.languageToString(language)
        }
    }

    override suspend fun updateYesterdayHours(hours: Int) {
        dataStore.edit {
            it[YesterdayHoursKey] = hours
        }
    }

    override suspend fun updateGracePeriodHours(hours: Int) {
        dataStore.edit {
            it[GracePeriodHoursKey] = hours
        }
    }
}