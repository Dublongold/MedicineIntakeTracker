package com.medicine.intake.tracker.ui.main.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun HomePageCompactHeightContent(
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
            medicineUiPadding = PaddingValues(
                start = LocalDimensions.current.pagePadding,
                top = LocalDimensions.current.pagePadding,
                end = LocalDimensions.current.defaultContentSpacing,
                bottom = LocalDimensions.current.pagePadding,
            ),
            contentPadding = PaddingValues(
                top = LocalDimensions.current.defaultContentSpacing,
            )
        ) { content ->
            val medicine = state.currentMedicine ?: return@MedicineAwareContent
            Row {
                content(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    RecentIntakesList(
                        medicineName = medicine.name,
                        intakes = state.recentIntakes,
                        onHistoryClicked = onHistoryClicked,
                        onDeleteClick = onDeleteClick,
                        listPadding = LocalDimensions.current.run {
                            PaddingValues(bottom = pagePadding - listSpacing)
                        },
                        noIntakesModifier = Modifier.padding(bottom = LocalDimensions.current.pagePadding),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(
                                end = LocalDimensions.current.defaultContentSpacing,
                                start = LocalDimensions.current.pagePadding
                            )
                    )
                }
                TakeMedicineButton(
                    medicineName = medicine.name, onClick = {
                        onAddIntake(medicine)
                    }, onLongClick = {
                        onAddCustomIntake(medicine)
                    }, enabled = state.canTakeMedicine, modifier = Modifier
                        .padding(
                            bottom = LocalDimensions.current.pagePadding,
                            top = LocalDimensions.current.pagePadding,
                            end = LocalDimensions.current.pagePadding,
                        )
                        .fillMaxHeight()
                        .fillMaxWidth(0.4f)
                )
            }
        }
    } else {
        LoadingPlaceholder(modifier)
    }
}

@Preview(
    device = "spec:width=411dp,height=891dp,orientation=landscape"
)
@Composable
private fun HomePageCompactHeightContentPreview() {
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
    HomePageCompactHeightContent(
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