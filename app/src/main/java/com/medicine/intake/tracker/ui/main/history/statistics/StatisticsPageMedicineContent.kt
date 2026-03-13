package com.medicine.intake.tracker.ui.main.history.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.createFakeIntakes
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.createFakeMedicines
import com.medicine.intake.tracker.domain.statistics.medicine.MedicineStatisticsState
import com.medicine.intake.tracker.domain.statistics.medicine.MedicineStatisticsUseCase
import com.medicine.intake.tracker.ui.composable.MedicineDropdownMenu
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import java.time.Duration

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatisticsPageMedicineContent(
    state: MedicineStatisticsState,
    topPadding: Dp,
    onSelectedMedicine: (MedicineId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val widthIsExpanded =
        windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        )
    val widthIsMedium =
        windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) && !widthIsExpanded

    val itemsInRow = when {
        widthIsExpanded -> 3
        widthIsMedium -> 2
        else -> 1
    }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.defaultContentSpacing),
        contentPadding = PaddingValues(
            start = LocalDimensions.current.pagePadding,
            top = topPadding,
            end = LocalDimensions.current.pagePadding,
            bottom = LocalDimensions.current.pagePadding,
        )
    ) {
        item {
            MedicineDropdownMenu(
                selectedId = state.currentMedicineId,
                values = state.medicines,
                onSelected = onSelectedMedicine,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            FlowRow(
                modifier = Modifier,
                maxItemsInEachRow = itemsInRow,
                horizontalArrangement = Arrangement.spacedBy(
                    LocalDimensions.current.defaultContentSpacing,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(
                    LocalDimensions.current.defaultContentSpacing,
                    Alignment.CenterVertically
                ),
            ) {
                val itemModifier = Modifier.weight(1f)
                LargeStatisticsItem(
                    label = {
                        Text(stringResource(R.string.statistics_for_medicine_total_intakes))
                    },
                    value = {
                        Text(state.medicineTotalIntakesCount.toString())
                    },
                    modifier = itemModifier
                )
                LargeStatisticsItem(
                    label = {
                        Text(stringResource(R.string.statistics_for_medicine_adherence))
                    },
                    value = {
                        Text(percentFormat.format(state.adherence))
                    },
                    modifier = itemModifier
                )
                LargeStatisticsItem(
                    label = {
                        Text(stringResource(R.string.statistics_for_medicine_best_intakes_streak))
                    },
                    value = {
                        Text(state.bestStreak.toString())
                    },
                    modifier = itemModifier
                )
                LargeStatisticsItem(
                    label = {
                        Text(stringResource(R.string.statistics_for_medicine_average_intakes_per_day))
                    },
                    value = {
                        Text(state.averageIntakesPerDay.toString())
                    },
                    modifier = itemModifier
                )
                LargeStatisticsItem(
                    label = {
                        Text(stringResource(R.string.statistics_for_medicine_shortest_interval_between_intakes))
                    },
                    value = {
                        Text(state.shortestIntervalBetweenIntakes.toUiString())
                    },
                    modifier = itemModifier
                )
                LargeStatisticsItem(
                    label = {
                        Text(stringResource(R.string.statisitcs_for_medicine_longest_interval_between_intakes))
                    },
                    value = {
                        Text(state.longestIntervalBetweenIntakes.toUiString())
                    },
                    modifier = itemModifier
                )
                LargeStatisticsItem(
                    label = {
                        Text(stringResource(R.string.statistics_for_medicine_average_interval_between_intakes))
                    },
                    value = {
                        Text(state.averageIntervalBetweenIntakes.toUiString())
                    },
                    modifier = itemModifier
                )
            }

        }
    }
}

@Composable
fun LargeStatisticsItem(
    label: @Composable () -> Unit,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = LocalDimensions.current.itemCardPadding,
                    horizontal = LocalDimensions.current.itemCardPadding * 2
                )
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                LocalDimensions.current.connectedItemsSpacing, Alignment.CenterVertically
            )
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            ) {
                label()
            }
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center
                )
            ) {
                value()
            }
        }
    }
}

@Composable
fun Duration.toUiString(): String {
    val totalSeconds = this.seconds
    if (totalSeconds == 0L) return "0 m"

    // Standard conversions
    val months = totalSeconds / (30 * 24 * 3600)
    val days = (totalSeconds % (30 * 24 * 3600)) / (24 * 3600)
    val hours = (totalSeconds % (24 * 3600)) / 3600
    val minutes = (totalSeconds % 3600) / 60

    return buildString {
        var isFirst = true
        fun appendWithSpaceIfNotFirst(value: String) {
            if (!isFirst) {
                append(" $value")
            } else {
                append(value)
                isFirst = false
            }
        }
        if (months > 0) {
            appendWithSpaceIfNotFirst(stringResource(R.string.months_short, months))
        }
        if (days > 0) {
            appendWithSpaceIfNotFirst(stringResource(R.string.days_short, days))
        }
        if (hours > 0) {
            appendWithSpaceIfNotFirst(stringResource(R.string.hours_short, hours))
        }
        if (minutes > 0) {
            appendWithSpaceIfNotFirst(stringResource(R.string.minutes_short, minutes))
        }
    }.trim()
}

@Preview(device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun StatisticsPageMedicineContentPreview() {
    val useCase = remember {
        val medicines = createFakeMedicines()
        val intakes = createFakeIntakes()
        val currentMedicineId =
            1
        medicines.filter { intakes.any { intake -> intake.medicineId == it.id } }.random().id
        MedicineStatisticsUseCase(
            currentMedicineId = currentMedicineId,
            medicines = medicines,
            intakes = intakes,
            gracePeriodHours = 2,
        )
    }
    Scaffold {
        StatisticsPageMedicineContent(
            state = remember(useCase) {
                useCase.createState()
            },
            topPadding = 0.dp,
            onSelectedMedicine = {},
            modifier = Modifier.padding(it)
        )
    }
}