package com.medicine.intake.tracker.ui.main.history.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun StatisticsItem(
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
                    vertical = LocalDimensions.current.smallItemCardPadding,
                    horizontal = LocalDimensions.current.smallItemCardPadding * 2
                )
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                LocalDimensions.current.connectedItemsSpacing, Alignment.CenterVertically
            )
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center
                )
            ) {
                label()
            }
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                )
            ) {
                value()
            }
        }
    }
}
