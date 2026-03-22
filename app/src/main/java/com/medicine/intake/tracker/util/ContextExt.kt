package com.medicine.intake.tracker.util

import android.content.Context
import android.os.LocaleList
import com.medicine.intake.tracker.domain.settings.Language

fun Context.updateLocale(language: Language): Context {
    val locale = language.locale
    if (locale != null) {
        val localeList = LocaleList(locale)

        val configuration = resources.configuration
        configuration.setLocales(localeList)
        return createConfigurationContext(configuration)
    } else {
        return this
    }
}