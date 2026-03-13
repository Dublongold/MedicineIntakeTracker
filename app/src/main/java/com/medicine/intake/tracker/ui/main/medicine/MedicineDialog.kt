package com.medicine.intake.tracker.ui.main.medicine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineIcon
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun MedicineDialog(
    medicine: Medicine,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val intakesPerDay = stringResource(
        R.string.medicine_intakes_per_day,
        medicine.intakesPerDay
    )
    val actionEdit = stringResource(R.string.action_edit)
    val actionClose = stringResource(R.string.action_close)
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = {
            Text(text = medicine.name, Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        },
        text = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val description = medicine.description
                if (description != null) {
                    Text(text = description, textAlign = TextAlign.Center)
                }
                Text(
                    text = intakesPerDay, textAlign = TextAlign.Center
                )
            }
        },
        icon = medicine.icon?.let { icon ->
            {
                Icon(
                    icon.painter,
                    icon.contentDescription,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onEditClick) {
                Text(actionEdit)
            }
        },
        dismissButton = {
            TextButton(onDismissRequest) {
                Text(actionClose)
            }
        }
    )
}

@Preview
@Composable
private fun MedicineDialogPreview() {
    MedicineDialog(
        medicine = Medicine(
            0,
            name = "Medicine Name",
            description = "Medicine Description",
//            description = "Very Long Medicine Description That The User Made For No Reason",
//            description = null,
            intakesPerDay = 3,
            icon = MedicineIcon.Bottle
        ),
        onDismissRequest = {},
        onEditClick = {},
    )
}