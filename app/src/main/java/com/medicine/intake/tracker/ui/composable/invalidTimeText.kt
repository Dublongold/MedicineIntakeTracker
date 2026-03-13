package com.medicine.intake.tracker.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.medicine.intake.tracker.R
import kotlin.math.max

@Composable
fun invalidTimeText(
    min: String?,
    max: String?,
    current: String
) = when {
    min == null && max != null -> stringResource(
        R.string.invalid_time_with_max, max, current
    )
    min != null && max == null -> stringResource(
        R.string.invalid_time_with_min, min, current
    )
    min != null && max != null -> stringResource(
        R.string.invalid_time_in_range, min, max, current
    )
    else -> ""
}