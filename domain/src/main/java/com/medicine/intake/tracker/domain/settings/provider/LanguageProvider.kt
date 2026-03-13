package com.medicine.intake.tracker.domain.settings.provider

import com.medicine.intake.tracker.domain.settings.Language
import kotlinx.coroutines.flow.Flow

interface LanguageProvider {
    val language: Flow<Language>
}