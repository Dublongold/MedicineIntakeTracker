package com.medicine.intake.tracker.ui.main.home.dialog.customintake

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R

@Composable
fun IntakesLimitDialog(
    medicineName: String,
    intakesPerDay: Int,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest,
        title = { Text(stringResource(R.string.home_intakes_limit_dialog_title, medicineName)) },
        text = {
            Text(
                stringResource(
                    R.string.home_intakes_limit_dialog_message,
                    intakesPerDay,
                    medicineName
                )
            )
        },
        confirmButton = {
            TextButton(onConfirm) {
                Text(stringResource(R.string.action_yes))
            }
        }, dismissButton = {
            TextButton(onDismissRequest) {
                Text(stringResource(R.string.action_no))
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