package com.medicine.intake.tracker.ui.composable.medicine

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowSizeClass
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.createFakeMedicines
import com.medicine.intake.tracker.ui.composable.MedicineDropdownMenu
import com.medicine.intake.tracker.ui.composable.VerticalArrows
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun AnimatedMedicineDropdownMenu(
    visible: Boolean,
    selectedId: MedicineId?,
    values: List<Medicine>,
    onSelected: (MedicineId) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + expandVertically { -it },
            exit = fadeOut() + shrinkVertically { -it },
        ) {
            MedicineDropdownMenu(
                selectedId = selectedId,
                values = values,
                onSelected = onSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
            )
        }
        AnimatedVisibility(
            visible = !visible,
            enter = fadeIn() + expandVertically { -it },
            exit = fadeOut() + shrinkVertically { -it },
        ) {
            Spacer(Modifier.padding(contentPadding))
        }
    }
}

typealias ContentFunction = @Composable (Modifier, @Composable ColumnScope.(medicinesIsVisible: Boolean) -> Unit) -> Unit

@Composable
fun MedicineAwareContent(
    currentMedicineId: MedicineId?,
    medicines: List<Medicine>,
    onCreateNewMedicine: () -> Unit,
    onMedicineSelected: (MedicineId) -> Unit,
    modifier: Modifier = Modifier,
    medicineUiPadding: PaddingValues = PaddingValues(LocalDimensions.current.pagePadding),
    contentPadding: PaddingValues = PaddingValues(
        start = LocalDimensions.current.pagePadding,
        end = LocalDimensions.current.pagePadding,
        bottom = LocalDimensions.current.pagePadding,
    ),
    content: @Composable (ContentFunction) -> Unit
) {
    @Composable
    fun Content(modifier: Modifier, content: @Composable ColumnScope.(medicinesIsVisible: Boolean) -> Unit) {
        val windowAdaptiveInfo = currentWindowAdaptiveInfo()
        val haveEnoughHeight = windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
            heightDpBreakpoint = WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        )
        val layoutDirection = LocalLayoutDirection.current

        if (haveEnoughHeight) {
            Column(modifier) {
                AnimatedMedicineDropdownMenu(
                    visible = medicines.size > 1,
                    selectedId = currentMedicineId,
                    values = medicines,
                    onSelected = onMedicineSelected,
                    contentPadding = PaddingValues(
                        top = medicineUiPadding.calculateTopPadding(),
                        end = medicineUiPadding.calculateEndPadding(layoutDirection),
                        start = medicineUiPadding.calculateStartPadding(layoutDirection)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                content(true)
            }
        } else {
            var expanded by remember { mutableStateOf(true) }
            Row(modifier) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    AnimatedMedicineDropdownMenu(
                        visible = medicines.size > 1 && expanded,
                        selectedId = currentMedicineId,
                        values = medicines,
                        onSelected = onMedicineSelected,
                        contentPadding = PaddingValues(
                            top = medicineUiPadding.calculateTopPadding(),
                            start = medicineUiPadding.calculateStartPadding(layoutDirection)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    content(expanded)
                }
                VerticalArrows(
                    onClickUp = {
                        expanded = false
                    },
                    onClickDown = {
                        expanded = true
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            start = LocalDimensions.current.defaultContentSpacing,
                            top = medicineUiPadding.calculateTopPadding(),
                            bottom = medicineUiPadding.calculateBottomPadding(),
                            end = medicineUiPadding.calculateEndPadding(layoutDirection)
                        ),
                    upEnabled = expanded,
                    downEnabled = !expanded
                )
            }
        }
    }
    when {
        medicines.isEmpty() -> {
            NoMedicinesContent(
                onCreateNewMedicine = onCreateNewMedicine,
                modifier = modifier.padding(contentPadding)
            )
        }

        currentMedicineId == null -> {
            Content(Modifier) {
                NoMedicineSelectedContent(Modifier.padding(contentPadding))
            }
        }

        else -> {
            Box(modifier) {
                content(::Content)
            }
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Preview(device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun MedicineAwareContentPreview() {
    val medicines: List<Medicine> = remember {
        createFakeMedicines()
//        emptyList()
    }
    val currentMedicineId: MedicineId? = remember {
//        null
        medicines.randomOrNull()?.id
    }
    Scaffold { paddingValues ->
        MedicineAwareContent(
            modifier = Modifier.padding(paddingValues),

            currentMedicineId = currentMedicineId,
            medicines = medicines,
            onCreateNewMedicine = {

            },
            onMedicineSelected = {

            },
            content = {
                it(Modifier) {
                    Text("Content")
                }
            }
        )
    }
}