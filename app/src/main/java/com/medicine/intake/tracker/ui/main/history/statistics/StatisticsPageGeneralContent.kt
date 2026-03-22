package com.medicine.intake.tracker.ui.main.history.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.createFakeIntakes
import com.medicine.intake.tracker.domain.medicine.createFakeMedicines
import com.medicine.intake.tracker.domain.statistics.general.GeneralStatisticsState
import com.medicine.intake.tracker.domain.statistics.general.GeneralStatisticsUseCase
import com.medicine.intake.tracker.ui.main.medicine.contentDescription
import com.medicine.intake.tracker.ui.main.medicine.painter
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun StatisticsPageGeneralContent(
    state: GeneralStatisticsState,
    topPadding: Dp,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val defaultItemModifier =
        Modifier.padding(horizontal = LocalDimensions.current.pagePadding)
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            top = topPadding,
            bottom = LocalDimensions.current.pagePadding,
        ),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.defaultContentSpacing)
    ) {
        item {
            var maxCardHeight by remember {
                mutableStateOf<Dp?>(null)
            }
            val pagePadding = LocalDimensions.current.pagePadding
            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = LocalDimensions.current.pagePadding
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent {
                        drawContent()
                        val fadingPercent = pagePadding.toPx() / size.width
                        drawRect(
                            Brush.horizontalGradient(
                                0f to Color.Red,
                                fadingPercent to Color.Transparent,
                                (1f - fadingPercent) to Color.Transparent,
                                1f to Color.Red,
                            ),
                            blendMode = BlendMode.DstOut
                        )
                    },
                horizontalArrangement = Arrangement.spacedBy(
                    LocalDimensions.current.listSpacing,
                    Alignment.CenterHorizontally
                )
            ) {
                val itemModifier = Modifier
                    .height(maxCardHeight ?: Dp.Unspecified)
                    .onPlaced {
                        maxCardHeight = maxOf(
                            maxCardHeight?.value ?: 0f, it.size.height / density.density
                        ).dp
                    }
                item {
                    StatisticsItem(label = {
                        Text(stringResource(R.string.statistics_general_total_intakes))
                    }, value = {
                        Text(state.totalIntakesCount.toString())
                    }, modifier = itemModifier)
                }
                item {
                    StatisticsItem(label = {
                        Text(stringResource(R.string.statistics_general_medicines_completed))
                    }, value = {
                        Text(state.numberOfCompletedMedicines.toString())
                    }, modifier = itemModifier)
                }
                state.medicineWithBestStreak?.also { medicineWithBestStreak ->
                    item {
                        StatisticsItem(label = {
                            Text(stringResource(R.string.statistics_general_medicine_with_the_best_intakes_streak))
                        }, value = {
                            val (medicine, streak) = medicineWithBestStreak
                            val icon = medicine.icon
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (icon != null) {
                                    Icon(
                                        painter = icon.painter,
                                        contentDescription = icon.contentDescription,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                                Text(medicine.name)
                                Text(
                                    stringResource(
                                        R.string.statistics_general_medicine_with_the_best_intakes_streak_value,
                                        streak
                                    ))
                            }
                        }, modifier = itemModifier)
                    }
                }
                state.medicineWithBestAdherence?.also { medicineWithBestAdherence ->
                    item {
                        StatisticsItem(label = {
                            Text(stringResource(R.string.statistics_general_medicine_with_the_best_adherence))
                        }, value = {
                            val (medicine, adherence) = medicineWithBestAdherence
                            val icon = medicine.icon
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (icon != null) {
                                    Icon(
                                        painter = icon.painter,
                                        contentDescription = icon.contentDescription,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                                Text(medicine.name)
                                Text("(${percentFormat.format(adherence)})")
                            }
                        }, modifier = itemModifier)
                    }
                }
            }
        }
        item {
            StatisticsList(
                remember(state.topMedicinesWithMostIntakes) {
                    StatisticsListValue(
                        label = { Text(
                            stringResource(
                                R.string.statistics_general_top_medicines_with_most_intakes,
                                state.topMedicinesWithMostIntakes.size
                            )) },
                        items = state.topMedicinesWithMostIntakes.map { (medicine, count) ->
                            StatisticsListValue.Item(
                                text = {
                                    val icon = medicine.icon
                                    if (icon != null) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                LocalDimensions.current.defaultContentSpacing
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                icon.painter,
                                                icon.contentDescription,
                                                Modifier.size(24.dp)
                                            )
                                            Text(medicine.name)
                                        }
                                    } else {
                                        Text(medicine.name)
                                    }
                                },
                                value = {
                                    Text(count.toString())
                                }
                            )
                        }
                    )
                },
                modifier = defaultItemModifier,
            )
        }
        item {
            StatisticsList(
                remember(state.topMedicinesWithBestStreak) {
                    StatisticsListValue(
                        label = { Text(
                            stringResource(
                                R.string.statistics_general_top_medicines_with_the_best_intakes_streak,
                                state.topMedicinesWithBestStreak.size
                            )) },
                        items = state.topMedicinesWithBestStreak.map { (medicine, count) ->
                            StatisticsListValue.Item(
                                text = {
                                    val icon = medicine.icon
                                    if (icon != null) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                LocalDimensions.current.defaultContentSpacing
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                icon.painter,
                                                icon.contentDescription,
                                                Modifier.size(24.dp)
                                            )
                                            Text(medicine.name)
                                        }
                                    } else {
                                        Text(medicine.name)
                                    }
                                },
                                value = {
                                    Text(count.toString())
                                }
                            )
                        }
                    )
                },
                modifier = defaultItemModifier,
            )
        }
        item {
            StatisticsList(
                remember(state.topMedicinesByAdherence) {
                    StatisticsListValue(
                        label = { Text(
                            stringResource(
                                R.string.statistics_general_top_medicines_by_adherence,
                                state.topMedicinesByAdherence.size
                            )) },
                        items = state.topMedicinesByAdherence.map { (medicine, count) ->
                            StatisticsListValue.Item(
                                text = {
                                    val icon = medicine.icon
                                    if (icon != null) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                LocalDimensions.current.defaultContentSpacing
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                icon.painter,
                                                icon.contentDescription,
                                                Modifier.size(24.dp)
                                            )
                                            Text(medicine.name)
                                        }
                                    } else {
                                        Text(medicine.name)
                                    }
                                },
                                value = {
                                    Text(percentFormat.format(count))
                                }
                            )
                        }
                    )
                },
                modifier = defaultItemModifier,
            )
        }
    }
}

@Preview
@Composable
private fun StatisticsPageGeneralContentPreview() {
    val useCase = remember {
        val intakes = createFakeIntakes()
        val medicines = createFakeMedicines()
        val gracePeriodHours = 2
        GeneralStatisticsUseCase(
            intakes = intakes,
            medicines = medicines,
            gracePeriodHours = gracePeriodHours
        )
    }
    Scaffold {
        StatisticsPageGeneralContent(
            state = remember { useCase.createState() },
            modifier = Modifier.padding(it),
            topPadding = 0.dp
        )
    }
}