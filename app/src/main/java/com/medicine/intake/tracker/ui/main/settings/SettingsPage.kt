package com.medicine.intake.tracker.ui.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.FakeIntakeRepository
import com.medicine.intake.tracker.domain.medicine.FakeMedicineRepository
import com.medicine.intake.tracker.domain.settings.FakeSettingsRepository
import com.medicine.intake.tracker.domain.settings.Language
import com.medicine.intake.tracker.domain.settings.Theme
import com.medicine.intake.tracker.ui.composable.LoadingPlaceholder
import com.medicine.intake.tracker.ui.main.settings.dialog.ClearAllDataDialog
import com.medicine.intake.tracker.ui.main.settings.dialog.ClearAllIntakesDialog
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import org.koin.compose.viewmodel.koinViewModel

private val Theme.localizedString
    @Composable get() = when (this) {
        Theme.System -> stringResource(R.string.settings_theme_system)
        Theme.Light -> stringResource(R.string.settings_theme_light)
        Theme.Dark -> stringResource(R.string.settings_theme_dark)
    }

private val Language.localizedString
    @Composable get() = when (this) {
        Language.System -> stringResource(R.string.settings_language_system)
        Language.Russian -> stringResource(R.string.settings_language_russian)
        Language.Ukrainian -> stringResource(R.string.settings_language_ukrainian)
        Language.English -> stringResource(R.string.settings_language_english)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showClearAllDataDialog by remember { mutableStateOf(false) }
    var showClearAllIntakesDialog by remember { mutableStateOf(false) }

    when (val state = state) {
        is SettingsUiState.Loaded -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = LocalDimensions.current.pagePadding),
            ) {
                val pagePaddingPx = with(LocalDensity.current) {
                    LocalDimensions.current.pagePadding.toPx()
                }
                val horizontalLazyListModifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent() {
                        drawContent()
                        val end = size.width - pagePaddingPx
                        drawRect(
                            Brush.horizontalGradient(
                                0f to Color.Transparent,
                                (pagePaddingPx / size.width) to Color.Red,
                                (end / size.width) to Color.Red,
                                1f to Color.Transparent,
                            ), blendMode = BlendMode.DstIn
                        )
                    }
                Column {
                    Text(
                        stringResource(R.string.settings_theme),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = LocalDimensions.current.pagePadding)
                    )
                    LazyRow(
                        modifier = horizontalLazyListModifier,
                        contentPadding = PaddingValues(horizontal = LocalDimensions.current.pagePadding),
                        horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing)
                    ) {
                        items(Theme.entries) { theme ->
                            FilterChip(
                                selected = state.theme == theme,
                                onClick = { viewModel.updateTheme(theme) },
                                label = { Text(theme.localizedString) })
                        }
                    }
                }
                Spacer(Modifier.height(LocalDimensions.current.defaultContentSpacing))
                Column {
                    Text(
                        stringResource(R.string.settings_launguage),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = LocalDimensions.current.pagePadding)
                    )
                    LazyRow(
                        horizontalLazyListModifier,
                        contentPadding = PaddingValues(
                            horizontal = LocalDimensions.current.pagePadding
                        ),
                        horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing)
                    ) {
                        items(Language.entries) { language ->
                            FilterChip(
                                selected = state.language == language,
                                onClick = { viewModel.updateLanguage(language) },
                                label = { Text(language.localizedString) }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(LocalDimensions.current.defaultContentSpacing))
                SettingsSliderOption(
                    label = {
                        SettingsLabelWithDescription(
                            stringResource(R.string.settings_recent_intakes_visibility_label),
                            stringResource(R.string.settings_recent_intakes_visibility_description),
                        )
                    },
                    value = state.yesterdayHours.toFloat(),
                    onValueChange = {
                        viewModel.updateYesterdayHours(it.toInt())
                    },
                    min = 0f,
                    max = 24f,
                    steps = 23,
                    modifier = Modifier.fillMaxWidth(),
                    units = {
                        Text(
                            text = stringResource(
                                R.string.units_hours,
                                state.yesterdayHours
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )
                Spacer(Modifier.height(LocalDimensions.current.defaultContentSpacing))
                SettingsSliderOption(
                    label = {
                        SettingsLabelWithDescription(
                            stringResource(R.string.settings_extend_yesterday_label),
                            stringResource(R.string.settings_extend_yesterday_description),
                        )
                    },
                    value = state.gracePeriodHours.toFloat(),
                    onValueChange = {
                        viewModel.updateGracePeriodHours(it.toInt())
                    },
                    min = 0f,
                    max = 6f,
                    steps = 5,
                    modifier = Modifier.fillMaxWidth(),
                    units = {
                        Text(
                            text = stringResource(
                                R.string.units_hours,
                                state.gracePeriodHours
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )

                Spacer(Modifier.height(LocalDimensions.current.defaultContentSpacing))
                Spacer(Modifier.weight(1f))

                Column {
                    Button(
                        onClick = { showClearAllIntakesDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalDimensions.current.pagePadding),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(stringResource(R.string.settings_clear_all_intakes))
                    }
                    Button(
                        onClick = { showClearAllDataDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalDimensions.current.pagePadding),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(stringResource(R.string.settings_clear_all_data))
                    }
                }
            }
        }

        SettingsUiState.Loading -> {
            LoadingPlaceholder(modifier)
        }
    }

    if (showClearAllIntakesDialog) {
        ClearAllIntakesDialog(
            onConfirm = {
                viewModel.clearAllIntakes()
                showClearAllIntakesDialog = false
            },
            onDismissRequest = {
                showClearAllIntakesDialog = false
            },
        )
    }
    if (showClearAllDataDialog) {
        ClearAllDataDialog(
            onConfirm = {
                viewModel.clearAllData()
                showClearAllDataDialog = false
            },
            onDismissRequest = {
                showClearAllDataDialog = false
            },
        )
    }
}



@Preview
@Composable
private fun SettingsPagePreview() {
    Scaffold {
        SettingsPage(
            viewModel = viewModel {
                SettingsViewModel(
                    medicineDeleter = FakeMedicineRepository.withFakeData(),
                    settingsRepo = FakeSettingsRepository(),
                    intakeDeleter = FakeIntakeRepository.withFakeData()
                )
            }, modifier = Modifier.padding(it)
        )
    }
}