package com.medicine.intake.tracker.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.ui.composable.BackButton
import com.medicine.intake.tracker.ui.main.history.HistoryNavKey
import com.medicine.intake.tracker.ui.main.history.HistoryPage
import com.medicine.intake.tracker.ui.main.history.statistics.StatisticsPage
import com.medicine.intake.tracker.ui.main.home.HomePage
import com.medicine.intake.tracker.ui.main.medicine.MedicineNavKey
import com.medicine.intake.tracker.ui.main.medicine.MedicinePage
import com.medicine.intake.tracker.ui.main.medicine.edit.MedicineEditPage
import com.medicine.intake.tracker.ui.main.settings.SettingsPage
import kotlinx.coroutines.launch

private val NavBarIcons = listOf(
    R.drawable.ic_home,
    R.drawable.ic_medicine,
    R.drawable.ic_history,
    R.drawable.ic_settings
)

private const val HOME_PAGE = 0
private const val MEDICINE_PAGE = 1
private const val HISTORY_PAGE = 2
private const val SETTINGS_PAGE = 3

private val Int.navBarLabel
    @Composable
    get() = when (this) {
        0 -> stringResource(R.string.main_page_home)
        1 -> stringResource(R.string.main_page_medicine)
        2 -> stringResource(R.string.main_page_history)
        3 -> stringResource(R.string.main_page_settings)
        else -> stringResource(R.string.main_page_error)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val pagerState = rememberPagerState { 4 }
    val scope = rememberCoroutineScope()
    val navBarIcons = NavBarIcons.map { painterResource(it) }


    NavigationSuiteScaffold(
        navigationSuiteItems = {

            for ((index, icon) in navBarIcons.withIndex()) {
                item(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    label = { Text(text = index.navBarLabel, textAlign = TextAlign.Center) },
                    icon = {
                        Icon(icon, index.navBarLabel)
                    }
                )
            }
        },
    ) {
        val medicineBackStack = rememberNavBackStack(MedicineNavKey.Main)
        val historyBackStack = rememberNavBackStack(HistoryNavKey.Main)

        val medicineEditingNow =
            medicineBackStack.lastOrNull() is MedicineNavKey.Edit && pagerState.currentPage == MEDICINE_PAGE

        val statisticsIsVisible =
            historyBackStack.lastOrNull() == HistoryNavKey.Statistics && pagerState.currentPage == HISTORY_PAGE



        BackHandler(
            medicineEditingNow || statisticsIsVisible || pagerState.currentPage != HOME_PAGE
        ) {
            when {
                medicineEditingNow -> medicineBackStack.removeLastOrNull()
                statisticsIsVisible -> historyBackStack.removeLastOrNull()
                else -> scope.launch {
                    pagerState.animateScrollToPage(HOME_PAGE)
                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        when {
                            medicineEditingNow -> {
                                val navKey = medicineBackStack.lastOrNull() as? MedicineNavKey.Edit
                                if (navKey != null) {
                                    if (navKey.medicineId != null) {
                                        stringResource(R.string.main_page_medicine_edit)
                                    } else {
                                        stringResource(R.string.main_page_medicine_add)
                                    }
                                } else ""
                            }
                            statisticsIsVisible -> {
                                stringResource(R.string.main_page_statistics)
                            }
                            else -> pagerState.currentPage.navBarLabel
                        }
                    ) },
                    navigationIcon = {
                        AnimatedVisibility(
                            visible = medicineEditingNow || statisticsIsVisible,
                            enter = slideInHorizontally { -it } + expandHorizontally { -it },
                            exit = slideOutHorizontally { -it } + shrinkHorizontally { -it }
                        ) {
                            BackButton({
                                val editNavKey =
                                    medicineBackStack.lastOrNull() as? MedicineNavKey.Edit
                                val statisticsNavKey =
                                    historyBackStack.lastOrNull() as? HistoryNavKey.Statistics

                                when(pagerState.currentPage) {
                                    HISTORY_PAGE -> if (statisticsNavKey != null) {
                                        historyBackStack.remove(statisticsNavKey)
                                    }
                                    MEDICINE_PAGE -> if (editNavKey != null) {
                                        medicineBackStack.remove(editNavKey)
                                    }
                                }

                            }, enabled = medicineEditingNow || statisticsIsVisible)
                        }
                    },
                    actions = {
                        AnimatedVisibility(
                            visible = pagerState.currentPage == HISTORY_PAGE && !statisticsIsVisible,
                            enter = slideInHorizontally { it } + expandHorizontally { it },
                            exit = slideOutHorizontally { it } + shrinkHorizontally { it }
                        ) {
                            IconButton({
                                if (historyBackStack.lastOrNull() != HistoryNavKey.Statistics) {
                                    historyBackStack.add(HistoryNavKey.Statistics)
                                }
                            }) {
                                Icon(
                                    painterResource(R.drawable.ic_statistics),
                                    null
                                )
                            }
                        }
                    }
                )
            },
        ) { paddingValues ->
            @Composable
            fun PagerScope.PagerContent(page: Int) {
                when (page) {
                    0 -> HomePage(onHistoryClicked = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }, onCreateNewMedicine = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                        if (medicineBackStack.lastOrNull() !is MedicineNavKey.Edit) {
                            medicineBackStack.add(MedicineNavKey.Edit(null))
                        }
                    }, modifier = Modifier.padding(paddingValues))

                    1 -> {
                        DisposableEffect(Unit) {
                            onDispose {
                                medicineBackStack.dropLast(
                                    (medicineBackStack.size - 1).coerceAtLeast(
                                        0
                                    )
                                )
                            }
                        }
                        NavDisplay(backStack = medicineBackStack) { navKey ->
                            NavEntry(navKey) { navKey ->
                                when (navKey) {
                                    MedicineNavKey.Main -> {
                                        MedicinePage(
                                            modifier = Modifier.padding(paddingValues),
                                            onEdit = { id ->
                                                if (medicineBackStack.none { it is MedicineNavKey.Edit }) {
                                                    medicineBackStack.add(
                                                        MedicineNavKey.Edit(id)
                                                    )
                                                }
                                            })
                                    }

                                    is MedicineNavKey.Edit -> {
                                        MedicineEditPage(
                                            medicineId = navKey.medicineId,
                                            onConfirm = {
                                                medicineBackStack.remove(navKey)
                                            },
                                            onDelete = {
                                                medicineBackStack.remove(navKey)
                                            }, modifier = Modifier.padding(paddingValues)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        DisposableEffect(Unit) {
                            onDispose {
                                historyBackStack.dropLast(
                                    (historyBackStack.size - 1).coerceAtLeast(
                                        0
                                    )
                                )
                            }
                        }

                        NavDisplay(backStack = historyBackStack) { navKey ->
                            NavEntry(navKey) { navKey ->
                                when (navKey) {
                                    HistoryNavKey.Main -> HistoryPage(
                                        modifier = Modifier.padding(paddingValues),
                                        onCreateNewMedicine = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(1)
                                            }
                                            if (medicineBackStack.lastOrNull() !is MedicineNavKey.Edit) {
                                                medicineBackStack.add(MedicineNavKey.Edit(null))
                                            }
                                        })

                                    HistoryNavKey.Statistics -> {
                                        StatisticsPage(
                                            modifier = Modifier.padding(paddingValues)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    3 -> SettingsPage(modifier = Modifier.padding(paddingValues))
                }
            }
            if (currentWindowAdaptiveInfo().windowSizeClass.isWidthAtLeastBreakpoint(
                    WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
                ) && currentWindowAdaptiveInfo().windowSizeClass.isHeightAtLeastBreakpoint(
                    WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
                )
            ) {
                VerticalPager(state = pagerState, userScrollEnabled = false) { page ->
                    PagerContent(page)
                }
            } else {
                HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
                    PagerContent(page)
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}