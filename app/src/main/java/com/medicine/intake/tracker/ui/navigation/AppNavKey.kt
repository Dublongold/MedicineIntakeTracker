package com.medicine.intake.tracker.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface AppNavKey : NavKey {

    @Serializable
    object Main : AppNavKey
}