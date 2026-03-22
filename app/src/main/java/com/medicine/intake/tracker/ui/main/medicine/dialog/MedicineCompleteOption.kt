package com.medicine.intake.tracker.ui.main.medicine.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun MedicineCompleteOption(
    isCompleted: Boolean,
    onCompletedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = AlertDialogDefaults.containerColor
        ),
        onClick = {
            onCompletedChanged(!isCompleted)
        }
    ) {
        Row(
            modifier.padding(
                start = LocalDimensions.current.itemCardPadding
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Is completed?")
            Checkbox(
                checked = isCompleted,
                onCheckedChange = onCompletedChanged,
            )
        }
    }
}

@Preview
@Composable
private fun MedicineCompleteOptionPreview() {
    var isCompleted by remember { mutableStateOf(false) }
    MedicineCompleteOption(isCompleted = isCompleted, onCompletedChanged = {
        isCompleted = it
    })
}