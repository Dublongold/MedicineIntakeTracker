package com.medicine.intake.tracker.domain.settings.provider

import com.medicine.intake.tracker.domain.settings.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeProvider {
    val theme: Flow<Theme>
}