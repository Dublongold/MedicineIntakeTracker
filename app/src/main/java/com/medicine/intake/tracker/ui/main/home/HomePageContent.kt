package com.medicine.intake.tracker.ui.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.createFakeIntakes
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.createFakeMedicines
import com.medicine.intake.tracker.ui.composable.LoadingPlaceholder
import com.medicine.intake.tracker.ui.composable.medicine.MedicineAwareContent
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun HomePageContent(
    modifier: Modifier,
    onCreateNewMedicine: () -> Unit,
    onAddIntake: (Medicine) -> Unit,
    onAddCustomIntake: (Medicine) -> Unit,
    onHistoryClicked: () -> Unit,
    onSelectCurrentMedicine: (MedicineId) -> Unit,
    state: HomeUiState,
    onDeleteClick: (Intake) -> Unit
) {
    if (state is HomeUiState.Loaded) {
        MedicineAwareContent(
            currentMedicineId = state.currentMedicineId,
            medicines = state.medicines,
            onCreateNewMedicine = onCreateNewMedicine,
            onMedicineSelected = onSelectCurrentMedicine,
            modifier = modifier,
            medicineUiPadding = PaddingValues(LocalDimensions.current.pagePadding),
            contentPadding = PaddingValues(
                top = LocalDimensions.current.defaultContentSpacing,
            )
        ) { content ->
            content(Modifier) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(
                            start = LocalDimensions.current.pagePadding,
                            end = LocalDimensions.current.pagePadding,
                            bottom = LocalDimensions.current.pagePadding
                        )
                ) {
                    val medicine = state.currentMedicine ?: return@content
                    RecentIntakesList(
                        medicineName = medicine.name,
                        intakes = state.recentIntakes,
                        onHistoryClicked = onHistoryClicked,
                        onDeleteClick = onDeleteClick,
                        listPadding = PaddingValues(
                            bottom = LocalDimensions.current.run {
                                pagePadding - listSpacing
                            }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    HorizontalDivider()
                    TakeMedicineButton(
                        medicineName = medicine.name,
                        onClick = {
                            onAddIntake(medicine)
                        },
                        onLongClick = {
                            onAddCustomIntake(medicine)
                        },
                        enabled = state.canTakeMedicine,
                        modifier = Modifier
                            .padding(top = LocalDimensions.current.pagePadding)
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f)
                    )
                }
            }
        }
    } else {
        LoadingPlaceholder()
    }
}

@Preview
@Composable
private fun HomePageContentPreview() {

    val state = remember {
        val medicines = createFakeMedicines()
        HomeUiState.Loaded(
            canTakeMedicine = true,
            recentIntakes = createFakeIntakes(),
            currentMedicineId = medicines.random().id,
            medicines = medicines,
            gracePeriodHours = 2
        )
    }
    HomePageContent(
        modifier = Modifier,
        onCreateNewMedicine = {},
        onAddIntake = {},
        onAddCustomIntake = {},
        onHistoryClicked = {},
        onSelectCurrentMedicine = {},
        state = state,
        onDeleteClick = {}
    )
}