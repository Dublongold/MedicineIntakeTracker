package com.medicine.intake.tracker.ui.main.medicine

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineIcon
import com.medicine.intake.tracker.ui.theme.LocalDimensions


@Composable
fun MedicineCard(
    medicine: Medicine,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    onDeleteClick: (() -> Unit)? = null
) {
    val colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    val border = if (highlight) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(.5f))
    } else null
    if (onClick == null) {
        Card(
            modifier = modifier, colors = colors, border = border
        ) {
            MedicineCardContent(medicine, onDeleteClick)
        }
    } else {
        Card(
            modifier = modifier, colors = colors, onClick = onClick, border = border
        ) {
            MedicineCardContent(medicine, onDeleteClick)
        }
    }
}

@Composable
fun MedicineCardContent(
    medicine: Medicine, onDeleteClick: (() -> Unit)?, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LocalDimensions.current.itemCardPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing)
        ) {
            val icon = medicine.icon
            if (icon != null) {
                Icon(icon.painter, icon.contentDescription, Modifier.size(32.dp))
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.connectedItemsSpacing)
            ) {
                Text(medicine.name, style = MaterialTheme.typography.titleMedium)
                val description = medicine.description
                if (description != null) {
                    Text(
                        description,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Text(
                    stringResource(R.string.medicine_intakes_per_day, medicine.intakesPerDay),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        if (onDeleteClick != null) {
            IconButton(
                onDeleteClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ), modifier = Modifier.padding(start = LocalDimensions.current.listSpacing)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Удалить",
                )
            }
        }
    }
}

@Preview
@Composable
private fun MedicineCardPreview() {
    Column {
        MedicineCard(
            Medicine(
                0,
                name = "Medicine Name",
                description = null,
                intakesPerDay = 3,
                icon = MedicineIcon.BlisterPills
            ),
            {},
            Modifier.padding(16.dp),
        )
        MedicineCard(
            Medicine(
                1,
                name = "Medicine Name",
                description = "Medicine Description",
                intakesPerDay = 3,
                icon = null
            ), null, Modifier.padding(16.dp)
        )
        MedicineCard(
            Medicine(
                2,
                name = "Medicine Name",
                description = "Very Long Medicine Description That The User Made For No Reason",
                intakesPerDay = 3,
                icon = MedicineIcon.Bottle
            ), {}, Modifier.padding(16.dp), onDeleteClick = {})
    }
}