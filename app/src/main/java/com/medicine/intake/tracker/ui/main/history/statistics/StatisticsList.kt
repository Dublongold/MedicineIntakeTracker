package com.medicine.intake.tracker.ui.main.history.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import kotlin.random.Random

data class StatisticsListValue(
    val label: @Composable () -> Unit,
    val items: List<Item>
) {

    data class Item(
        val text: @Composable () -> Unit,
        val value: @Composable () -> Unit
    )
}


@Composable
fun StatisticsList(value: StatisticsListValue, modifier: Modifier = Modifier) {
    Column(
        modifier, verticalArrangement = Arrangement.spacedBy(
            LocalDimensions.current.listSpacing
        ), horizontalAlignment = Alignment.Start
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleMedium) {
            value.label()
        }
        HorizontalDivider()
        value.items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelLarge) {
                    item.text()
                }
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
                    item.value()
                }
            }
        }
    }
}

@Preview
@Composable
private fun StatisticsListPreview() {
    StatisticsList(
        remember {
            StatisticsListValue(
                label = {
                    Text("Label")
                },
                items = List(5) {
                    StatisticsListValue.Item(
                        text = { Text("Item $it") },
                        value = { Text("Value ${Random.nextInt(10, 100)}") }
                    )
                }
            )
        }
    )
}