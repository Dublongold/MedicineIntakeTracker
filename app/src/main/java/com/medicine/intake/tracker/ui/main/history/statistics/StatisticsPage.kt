package com.medicine.intake.tracker.ui.main.history.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.FakeIntakeRepository
import com.medicine.intake.tracker.domain.medicine.FakeMedicineRepository
import com.medicine.intake.tracker.domain.settings.FakeSettingsRepository
import com.medicine.intake.tracker.ui.composable.LoadingPlaceholder
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ColumnScope.DefaultVerticalAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn() + expandVertically { -it },
        exit = fadeOut() + shrinkVertically { -it },
        content = content
    )
}

@Composable
fun StatisticsPage(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val density = LocalDensity.current
    when (val state = state) {
        is StatisticsUiState.Loaded -> {

            Box(modifier) {
                var isGeneral by remember { mutableStateOf(true) }
                var topPadding by remember { mutableStateOf(0.dp) }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .onPlaced {
                            topPadding = (it.size.height / density.density).dp
                        }
                        .zIndex(1f)) {
                    DefaultVerticalAnimatedVisibility(state.medicines.isNotEmpty()) {
                        Column {
                            Spacer(Modifier.height(LocalDimensions.current.pagePadding))
                            SingleChoiceSegmentedButtonRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = LocalDimensions.current.pagePadding,
                                    ),
                            ) {
                                val colors = SegmentedButtonDefaults.colors()
                                    .copy(
                                        inactiveContainerColor = MaterialTheme.colorScheme.background
                                    )
                                SegmentedButton(
                                    isGeneral,
                                    onClick = { isGeneral = true },
                                    shape = SegmentedButtonDefaults.itemShape(0, 2),
                                    colors = colors,
                                ) {
                                    Text(stringResource(R.string.statistics_general))
                                }
                                SegmentedButton(
                                    !isGeneral,
                                    onClick = { isGeneral = false },
                                    shape = SegmentedButtonDefaults.itemShape(1, 2),
                                    colors = colors,
                                ) {
                                    Text(stringResource(R.string.statistics_for_medicine))
                                }
                            }
                            Spacer(Modifier.height(LocalDimensions.current.defaultContentSpacing))
                        }
                    }
                    DefaultVerticalAnimatedVisibility(state.medicines.isEmpty()) {
                        Spacer(Modifier.height(LocalDimensions.current.pagePadding))
                    }
                }
                if (!isGeneral && state.medicine != null) {
                    val pagerState = rememberPagerState { 2 }
                    LaunchedEffect(isGeneral) {
                        if (isGeneral) {
                            pagerState.animateScrollToPage(0)
                        } else {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                    HorizontalPager(
                        pagerState,
                        verticalAlignment = Alignment.Top,
                        userScrollEnabled = false
                    ) { page ->
                        when (page) {
                            1 -> {
                                StatisticsPageMedicineContent(
                                    state = state.medicine,
                                    topPadding = topPadding,
                                    onSelectedMedicine = viewModel::updateCurrentMedicineId,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            else -> {
                                StatisticsPageGeneralContent(
                                    state = state.general,
                                    topPadding = topPadding,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                } else {
                    StatisticsPageGeneralContent(
                        state = state.general,
                        topPadding = topPadding,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
//            MedicineAwareContent(
//                currentMedicineId = state.currentMedicineId,
//                medicines = state.medicines,
//                onCreateNewMedicine = onCreateNewMedicine,
//                onMedicineSelected = viewModel::updateCurrentMedicineId,
//                modifier = modifier,
//                content = {
//                    it(Modifier) {
//
//                        LazyColumn(
//                            contentPadding =
//                        ) {
//
//                        }
//                    }
//                })
        }

        StatisticsUiState.Loading -> {
            LoadingPlaceholder(modifier = modifier)
        }
    }
}


@Preview
@Composable
private fun StatisticsPagePreview() {
    Scaffold {
        StatisticsPage(
            modifier = Modifier.padding(it),
            viewModel = viewModel {
                val intakeRepository = FakeIntakeRepository.withFakeData()
                val medicineRepository = FakeMedicineRepository.withFakeData()
                val settingsRepository = FakeSettingsRepository()
                StatisticsViewModel(
                    intakeProvider = intakeRepository,
                    medicineProvider = medicineRepository,
                    currentMedicineIdUpdater = medicineRepository,
                    gracePeriodHoursProvider = settingsRepository
                )
            })
    }
}