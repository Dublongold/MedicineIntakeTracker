package com.medicine.intake.tracker.ui.main.medicine.dialog

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
import com.medicine.intake.tracker.ui.composable.CancelTextButton
import com.medicine.intake.tracker.ui.main.medicine.contentDescription
import com.medicine.intake.tracker.ui.main.medicine.painter

@Composable
fun DeleteMedicineDialog(
    medicine: Medicine,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDelete,
        modifier = modifier,
        title = { Text(stringResource(R.string.medicine_delete_dialog_title)) },
        text = { Text(stringResource(R.string.medicine_delete_dialog_message, medicine.name)) },
        icon = medicine.icon?.let {
            {
                Icon(
                    it.painter,
                    medicine.icon?.contentDescription,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(stringResource(R.string.action_delete), color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            CancelTextButton(onClick = onDismissRequest)
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