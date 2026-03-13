package com.medicine.intake.tracker.ui.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicine.intake.tracker.domain.intake.IntakeDeleter
import com.medicine.intake.tracker.domain.medicine.MedicineDeleter
import com.medicine.intake.tracker.domain.settings.Language
import com.medicine.intake.tracker.domain.settings.SettingsRepository
import com.medicine.intake.tracker.domain.settings.Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val medicineDeleter: MedicineDeleter,
    private val intakeDeleter: IntakeDeleter,
    private val settingsRepo: SettingsRepository
) : ViewModel() {
    val state: StateFlow<SettingsUiState> = combine(
        settingsRepo.theme,
        settingsRepo.language,
        settingsRepo.yesterdayHours,
        settingsRepo.gracePeriodHours,
    ) { theme, language, yesterdayHours, gracePeriodHours ->
        SettingsUiState.Loaded(
            theme = theme,
            language = language,
            yesterdayHours = yesterdayHours,
            gracePeriodHours = gracePeriodHours
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SettingsUiState.Loading)

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingsRepo.updateTheme(theme)
        }
    }

    fun updateLanguage(language: Language) {
        viewModelScope.launch {
            settingsRepo.updateLanguage(language)
        }
    }

    fun updateYesterdayHours(hours: Int) {
        viewModelScope.launch {
            settingsRepo.updateYesterdayHours(hours)
        }
    }

    fun updateGracePeriodHours(hours: Int) {
        viewModelScope.launch {
            settingsRepo.updateGracePeriodHours(hours)
        }
    }

    fun clearAllIntakes() {
        viewModelScope.launch {
            intakeDeleter.deleteAllIntakes()
        }
    }
    fun clearAllData() {
        viewModelScope.launch {
            medicineDeleter.deleteAllMedicines()
        }
    }
}