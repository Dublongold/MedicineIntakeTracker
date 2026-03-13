package com.medicine.intake.tracker.ui.composable.medicine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun NoMedicineSelectedContent(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize(),
        Alignment.Center
    ) {
        Text(
            stringResource(R.string.select_any_medicine_message),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun NoMedicineSelectedContentPreview() {
    NoMedicineSelectedContent()
}