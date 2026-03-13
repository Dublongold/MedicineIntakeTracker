package com.medicine.intake.tracker.ui.main.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun IntakesLimitDialog(
    medicineName: String,
    intakesPerDay: Int,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val title = "Intake %s?".format(medicineName)
    val text = "You have reached your daily limit of %d intakes for %s. Do you want to intake anyway?".format(
        intakesPerDay,
        medicineName
    )
    val actionConfirm = "Yes"
    val actionDismiss = "No"

    AlertDialog(
        onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onConfirm) {
                Text(actionConfirm)
            }
        }, dismissButton = {
            TextButton(onDismissRequest) {
                Text(actionDismiss)
            }
        }
    )
}

@Preview
@Composable
private fun IntakesLimitDialogPreview() {
    IntakesLimitDialog(
        medicineName = "Medicine",
        intakesPerDay = 3,
        onConfirm = {},
        onDismissRequest = {}
    )
}