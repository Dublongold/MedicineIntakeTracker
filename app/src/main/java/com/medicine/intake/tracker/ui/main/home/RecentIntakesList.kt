package com.medicine.intake.tracker.ui.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.createFakeIntakes
import com.medicine.intake.tracker.ui.composable.IntakeItem
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun RecentIntakesList(
    medicineName: String,
    intakes: List<Intake>?,
    onHistoryClicked: () -> Unit,
    onDeleteClick: (Intake) -> Unit,
    listPadding: PaddingValues,
    modifier: Modifier = Modifier,
    noIntakesModifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.home_recent_intakes),
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(
                onClick = onHistoryClicked,
                modifier = Modifier.padding(start = LocalDimensions.current.defaultContentSpacing)
            ) {
                Text(stringResource(R.string.home_full_history), textAlign = TextAlign.End)
            }
        }
        if (intakes.isNullOrEmpty()) {
            Box(
                noIntakesModifier
                    .fillMaxWidth()
                    .weight(1f), Alignment.Center
            ) {
                if (intakes == null) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        stringResource(R.string.home_no_recent_intakes_for, medicineName),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing),
                contentPadding = listPadding
            ) {
                item { Spacer(Modifier) }
                items(intakes) { intake ->
                    IntakeItem(
                        intake = intake, formatFullDate = true, onDeleteClick = {
                            onDeleteClick(intake)
                        })
                }
                item { Spacer(Modifier) }
            }
        }
    }
}

@Preview
@Composable
private fun RecentIntakesListPreview() {
    val intakes = remember {
        createFakeIntakes()
    }
    RecentIntakesList(
        medicineName = "Medicine",
        intakes = intakes,
        onHistoryClicked = {},
        onDeleteClick = {},
        listPadding = PaddingValues.Zero
    )
}