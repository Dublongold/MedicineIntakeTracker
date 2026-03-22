package com.medicine.intake.tracker.util

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.text.TextUtilsCompat
import com.medicine.intake.tracker.domain.settings.Language
import java.util.Locale

val Language.tag
    get() = when (this) {
        Language.English -> "en"
        Language.Russian -> "ru"
        Language.Ukrainian -> "uk"
        Language.System -> null
    }

val Language.locale
    get() = tag?.let { tag ->
        Locale.forLanguageTag(tag)
    }

@Composable
fun rememberLayoutDirection(language: Language): LayoutDirection {
    val currentLayoutDirection = LocalLayoutDirection.current
    return remember(language) {
        val locale = language.locale
        if (locale == null) {
            currentLayoutDirection
        } else {
            val androidDirection = TextUtilsCompat.getLayoutDirectionFromLocale(locale)

            if (androidDirection == View.LAYOUT_DIRECTION_RTL) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }
        }
    }
}