package com.medicine.intake.tracker.util

import android.content.Context
import android.os.LocaleList
import com.medicine.intake.tracker.domain.settings.Language
import java.util.Locale

fun Context.updateLocale(language: Language): Context {
    val languageTag = when(language) {
        Language.English -> "en"
        Language.Russian -> "ru"
        Language.Ukrainian -> "uk"
        Language.System -> null
    }
    if (languageTag != null) {
        val locale = Locale.forLanguageTag(languageTag)
        val localeList = LocaleList(locale)

        val configuration = resources.configuration
        configuration.setLocales(localeList)
        return createConfigurationContext(configuration)
    } else {
        return this
    }
}