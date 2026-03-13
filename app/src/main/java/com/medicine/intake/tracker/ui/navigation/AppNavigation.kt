package com.medicine.intake.tracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.medicine.intake.tracker.ui.main.MainScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(AppNavKey.Main)
    NavDisplay(backStack = backStack) {
        NavEntry(it) { navKey ->
            when (navKey) {
                AppNavKey.Main -> MainScreen()
            }
        }
    }
}

@Preview
@Composable
private fun AppNavigationPreview() {
    AppNavigation()
}