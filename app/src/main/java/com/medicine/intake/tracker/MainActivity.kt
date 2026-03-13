package com.medicine.intake.tracker

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.medicine.intake.tracker.data.mapper.SettingsMapper
import com.medicine.intake.tracker.domain.settings.SettingsDefaults
import com.medicine.intake.tracker.domain.settings.Theme
import com.medicine.intake.tracker.ui.navigation.AppNavigation
import com.medicine.intake.tracker.ui.theme.CalciumIntakeTrackerTheme
import com.medicine.intake.tracker.util.updateLocale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private val lightStatusBarStyle = SystemBarStyle.light(
    Color.TRANSPARENT,
    Color.TRANSPARENT,
)

private val darkStatusBarStyle = SystemBarStyle.dark(
    Color.TRANSPARENT,
)

private val autoStatusBarStyle = SystemBarStyle.auto(
    Color.TRANSPARENT,
    Color.TRANSPARENT,
)


class MainActivity : ComponentActivity() {
    private fun Theme.enableEdgeToEdge() {
        when (this) {
            Theme.System -> enableEdgeToEdge(autoStatusBarStyle, autoStatusBarStyle)
            Theme.Light -> enableEdgeToEdge(lightStatusBarStyle, lightStatusBarStyle)
            Theme.Dark -> enableEdgeToEdge(darkStatusBarStyle, darkStatusBarStyle)
        }
    }
    private val viewModel: MainViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val themeFromState = savedInstanceState?.getString("theme")?.let {
                SettingsMapper.stringToTheme(it)
            }
            var ignoreThisTime = false
            if (themeFromState != null) {
                themeFromState.enableEdgeToEdge()
                ignoreThisTime = true
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.theme.collect {
                    if (!ignoreThisTime) {
                        it.enableEdgeToEdge()
                    } else {
                        ignoreThisTime = false
                    }
                }
            }
        }
        setContent {
            val theme by viewModel.theme.collectAsState(
                savedInstanceState?.getString("theme")?.let {
                    SettingsMapper.stringToTheme(it)
                } ?: SettingsDefaults.DefaultTheme,
            )
            val language by viewModel.language.collectAsState(
                savedInstanceState?.getString("language")?.let {
                    SettingsMapper.stringToLanguage(it)
                } ?: SettingsDefaults.DefaultLanguage,
            )
            val updatedContext = remember(language, this) {
                updateLocale(language)
            }
            CompositionLocalProvider(LocalContext provides updatedContext) {
                CalciumIntakeTrackerTheme(theme = theme) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleScope.launch {
            val theme = viewModel.theme.first()
            outState.putString("theme", SettingsMapper.themeToString(theme))
            val language = viewModel.language.first()
            outState.putString("language", SettingsMapper.languageToString(language))
        }
    }
}