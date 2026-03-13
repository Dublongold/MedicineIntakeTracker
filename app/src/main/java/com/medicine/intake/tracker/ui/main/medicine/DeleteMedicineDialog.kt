package com.medicine.intake.tracker.ui.main.medicine

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.createFakeMedicines

@Composable
fun DeleteMedicineDialog(
    medicine: Medicine,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.medicine_delete_dialog_title)
    val message = stringResource(R.string.medicine_delete_dialog_message, medicine.name)
    val iconContentDescription = medicine.icon?.contentDescription
    val actionDelete = stringResource(R.string.action_delete)
    val actionCancel = stringResource(R.string.action_cancel)
    AlertDialog(
        onDismissRequest = onDelete,
        modifier = modifier,
        title = { Text(title) },
        text = { Text(message) },
        icon = medicine.icon?.let {
            {
                Icon(
                    it.painter,
                    iconContentDescription,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(actionDelete, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(actionCancel)
            }
        })
}

@Preview
@Composable
private fun DeleteMedicineDialogPreview() {
    val medicine = remember {
        createFakeMedicines().first()
    }
    DeleteMedicineDialog(medicine, onDelete = {}, onDismissRequest = {})
}