package com.medicine.intake.tracker.ui.main.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R

@Composable
fun TakeMedicineButton(
    medicineName: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonColors = ButtonDefaults.buttonColors()
    val contentColor by animateColorAsState(
        if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor
    )
    val containerColor by animateColorAsState(
        if (enabled) buttonColors.containerColor else buttonColors.disabledContainerColor
    )
    Box(
        modifier
            .background(containerColor, MaterialTheme.shapes.large)
            .clip(MaterialTheme.shapes.large)
            .combinedClickable(
                interactionSource = null,
                indication = ripple(color = contentColor),
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .padding(ButtonDefaults.ContentPadding),
        Alignment.Center,
    ) {
        Text(
            text = if (enabled) {
                stringResource(R.string.home_take_medicine, medicineName)
            } else stringResource(R.string.home_intake_unavailable),
            style = MaterialTheme.typography.titleMedium,
            color = contentColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun TakeMedicineButtonPreview() {
    TakeMedicineButton(
        medicineName = "Medicine Name",
        onClick = {},
        onLongClick = {}
    )
}