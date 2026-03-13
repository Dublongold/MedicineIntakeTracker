package com.medicine.intake.tracker

import androidx.lifecycle.ViewModel
import com.medicine.intake.tracker.domain.settings.SettingsRepository

class MainViewModel(
    settingsRepository: SettingsRepository
) : ViewModel() {
    val theme = settingsRepository.theme/*.stateIn(
        viewModelScope, SharingStarted.Eagerly, SettingsDefaults.DefaultTheme
    )*/
    val language = settingsRepository.language/*.stateIn(
        viewModelScope, SharingStarted.Eagerly, SettingsDefaults.DefaultLanguage
    )*/

}