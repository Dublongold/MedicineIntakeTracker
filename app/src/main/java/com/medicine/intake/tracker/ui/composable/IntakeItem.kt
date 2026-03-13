package com.medicine.intake.tracker.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import java.time.LocalDateTime

@Composable
fun IntakeItem(
    intake: Intake,
    formatFullDate: Boolean,
    onDeleteClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LocalDimensions.current.itemCardPadding,
                    vertical = if (onDeleteClick != null && !formatFullDate) {
                        LocalDimensions.current.itemCardPadding / 2
                    } else LocalDimensions.current.itemCardPadding
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (formatFullDate) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing)
                ) {
                    IntakeItemInfo(
                        icon = painterResource(R.drawable.ic_date),
                        contentDescription = "Дата",
                        value = intake.date
                    )
                    IntakeItemInfo(
                        icon = painterResource(R.drawable.ic_clock),
                        contentDescription = "Время",
                        value = intake.time
                    )
                }
            } else {
                IntakeItemInfo(
                    icon = painterResource(R.drawable.ic_clock),
                    contentDescription = "Время",
                    value = intake.time,
                    modifier = Modifier.weight(1f)
                )
            }
            if (onDeleteClick != null) {
                IconButton(
                    onDeleteClick, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Удалить",
                    )
                }
            }
        }
    }
}

@Composable
fun IntakeItemInfo(
    icon: Painter,
    contentDescription: String?,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun IntakeItemPreview() {
    val intake = remember {
        Intake(
            localDateTime = LocalDateTime.now(), medicineId = 0
        )
    }
    Column(
        Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        IntakeItem(intake, true, null)
        IntakeItem(intake, true, {})
        IntakeItem(intake, false, null)
        IntakeItem(intake, false, { })
    }
}