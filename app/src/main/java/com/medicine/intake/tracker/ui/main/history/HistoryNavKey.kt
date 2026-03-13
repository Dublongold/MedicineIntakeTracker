package com.medicine.intake.tracker.ui.main.history

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface HistoryNavKey : NavKey {
    @Serializable
    object Main : HistoryNavKey

    @Serializable
    object Statistics : HistoryNavKey
}