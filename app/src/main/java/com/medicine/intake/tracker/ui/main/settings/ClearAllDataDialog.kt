package com.medicine.intake.tracker.ui.main.settings

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R

@Composable
fun ClearAllDataDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.settings_clear_all_data_dialog_title)
    val message = stringResource(R.string.settings_clear_all_data_dialog_message)
    val delete = stringResource(R.string.action_clear)
    val cancel = stringResource(R.string.action_cancel)
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        icon = {
            Icon(
                painterResource(R.drawable.ic_delete),
                "Delete",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text(delete)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text(cancel) }
        })
}

@Preview
@Composable
private fun ClearAllDataDialogPreview() {
    ClearAllDataDialog(
        onConfirm = {},
        onDismissRequest = {}
    )
}