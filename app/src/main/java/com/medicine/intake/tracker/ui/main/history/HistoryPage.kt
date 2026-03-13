package com.medicine.intake.tracker.ui.main.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowSizeClass
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.FakeIntakeRepository
import com.medicine.intake.tracker.domain.medicine.FakeMedicineRepository
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.ui.composable.IntakeItem
import com.medicine.intake.tracker.ui.composable.LoadingPlaceholder
import com.medicine.intake.tracker.ui.composable.MedicineDropdownMenu
import com.medicine.intake.tracker.ui.composable.VerticalArrows
import com.medicine.intake.tracker.ui.composable.medicine.MedicineAwareContent
import com.medicine.intake.tracker.ui.composable.medicine.NoMedicinesContent
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryPage(
    onCreateNewMedicine: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    when (val state = state) {
        is HistoryUiState.Loaded -> {
            MedicineAwareContent(
                currentMedicineId = state.currentMedicineId,
                medicines = state.medicines,
                onCreateNewMedicine = onCreateNewMedicine,
                onMedicineSelected = viewModel::updateCurrentMedicineId,
                modifier = modifier,
            ) {
                it(Modifier) { medicineIsVisible ->
                    val topSpacing by animateDpAsState(
                        if (medicineIsVisible) {
                            LocalDimensions.current.defaultContentSpacing
                        } else {
                            0.dp
                        }
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            top = topSpacing,
                            start = LocalDimensions.current.pagePadding,
                            bottom = LocalDimensions.current.pagePadding,
                            end = if (windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
                                    WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)) {
                                LocalDimensions.current.pagePadding
                            } else 0.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.listSpacing),
                    ) {
                        state.history.entries.let { entries ->
                            entries.forEach { (date, intakes) ->
                                stickyHeader {
                                    Card(Modifier.fillMaxWidth()) {
                                        Text(
                                            text = date,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 8.dp
                                            )
                                        )
                                    }
                                }
                                items(intakes) { intake ->
                                    IntakeItem(
                                        intake = intake,
                                        formatFullDate = false,
                                        onDeleteClick = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        HistoryUiState.Loading -> {
            LoadingPlaceholder(modifier)
        }
    }
}

@Preview
@Preview(device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun HistoryPagePreview() {
    Scaffold {
        HistoryPage(
            viewModel = viewModel {
                val medicineRepository = FakeMedicineRepository.withFakeData(1)
                HistoryViewModel(
                    intakeProvider = FakeIntakeRepository.withFakeData(),
                    medicineProvider = medicineRepository,
                    currentMedicineIdUpdater = medicineRepository
                )
            }, modifier = Modifier
                .fillMaxSize()
                .padding(it),
            onCreateNewMedicine = {}
        )
    }
}