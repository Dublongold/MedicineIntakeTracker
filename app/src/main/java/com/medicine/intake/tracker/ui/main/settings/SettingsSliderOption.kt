package com.medicine.intake.tracker.ui.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun SettingsSliderOption(
    label: @Composable () -> Unit,
    value: Float,
    onValueChange: (Float) -> Unit,
    min: Float,
    max: Float,
    steps: Int,
    modifier: Modifier = Modifier,
    units: (@Composable RowScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .padding(horizontal = LocalDimensions.current.pagePadding)
            .width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.defaultContentSpacing),
    ) {
        label()
        if (units != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.defaultContentSpacing),
            ) {
                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    valueRange = min..max,
                    steps = steps,
                    modifier = Modifier.weight(1f)
                )
                units()
            }
        } else {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = min..max,
                steps = steps,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun SettingsSliderOptionPreview() {
    var value by remember { mutableFloatStateOf(0f) }
    SettingsSliderOption(
        label = {
            SettingsLabelWithDescription(
                "Label",
                "Description",
            )
        },
        value = value,
        onValueChange = { value = it },
        min = 1f,
        max = 6f,
        steps = 5
    )
}