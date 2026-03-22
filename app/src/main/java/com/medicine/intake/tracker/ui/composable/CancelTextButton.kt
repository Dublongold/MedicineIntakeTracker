package com.medicine.intake.tracker.ui.composable

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R

@Composable
fun CancelTextButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(stringResource(R.string.action_cancel))
    }
}

@Preview
@Composable
private fun CancelTextButtonPreview() {
    CancelTextButton(onClick = {

    })
}