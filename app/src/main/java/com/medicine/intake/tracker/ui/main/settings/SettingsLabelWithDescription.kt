package com.medicine.intake.tracker.ui.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun SettingsLabelWithDescription(
    label: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(LocalDimensions.current.connectedItemsSpacing),
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(description, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
private fun SettingsLabelWithDescriptionPreview() {
    SettingsLabelWithDescription("Label", "Description")
}