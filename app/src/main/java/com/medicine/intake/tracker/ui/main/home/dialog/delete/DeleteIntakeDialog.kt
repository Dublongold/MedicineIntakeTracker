package com.medicine.intake.tracker.ui.main.home.dialog.delete

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.ui.composable.CancelTextButton
import com.medicine.intake.tracker.util.rememberLocalDateTime

@Composable
fun DeleteIntakeDialog(
    medicineName: String,
    date: String,
    time: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localDateTime = rememberLocalDateTime(atStartOfMinute = true)
    val (dateNow, _) = IntakeMapper.localDateTimeToStrings(localDateTime)
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.home_delete_intake_dialog_title)) },
        text = {
            Text(
                if (dateNow != date) {
                    stringResource(
                        R.string.home_delete_intake_dialog_message,
                        medicineName,
                        date,
                        time
                    )
                } else {
                    stringResource(
                        R.string.home_delete_intake_dialog_message_for_today,
                        medicineName,
                        time
                    )
                }
            )
        },
        modifier = modifier,
        icon = {
            Icon(
                painterResource(R.drawable.ic_delete),
                null,
                Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.action_delete))
            }
        },
        dismissButton = {
            CancelTextButton(onClick = onDismissRequest)
        })
}

@Preview
@Composable
private fun DeleteIntakeDialogPreview() {
    DeleteIntakeDialog("Medicine Name", "2026-03-21", "12:00", {}, {})
}