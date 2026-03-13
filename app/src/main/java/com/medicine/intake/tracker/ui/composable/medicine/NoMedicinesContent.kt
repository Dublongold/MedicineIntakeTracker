package com.medicine.intake.tracker.ui.composable.medicine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun NoMedicinesContent(onCreateNewMedicine: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            LocalDimensions.current.spaceBetweenTextAndButton,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.no_medicines_available_message),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onCreateNewMedicine,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 42.dp)
        ) {
            Text(stringResource(R.string.no_medicines_available_button))
        }
    }
}

@Preview
@Composable
private fun NoMedicinesContentPreview() {
    NoMedicinesContent({})
}