package com.medicine.intake.tracker.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalDimensions = compositionLocalOf {
    Dimensions()
}

data class Dimensions(
    val pagePadding: Dp = 20.dp,
    val listSpacing: Dp = 8.dp,
    val defaultContentSpacing: Dp = 10.dp,
    val spaceBetweenTextAndButton: Dp = 12.dp,
    val connectedItemsSpacing: Dp = 6.dp,
    val itemCardPadding: Dp = 16.dp,
    val smallItemCardPadding: Dp = 8.dp,
)