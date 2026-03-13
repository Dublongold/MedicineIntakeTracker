package com.medicine.intake.tracker.ui.composable

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import java.time.LocalTime
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectionDialog(
    min: LocalTime?,
    max: LocalTime?,
    current: LocalTime,
    onConfirm: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val is24Hour = DateFormat.is24HourFormat(LocalContext.current)
    val context = LocalContext.current
    val state = remember(context) {
        TimePickerState(
            initialHour = current.hour,
            initialMinute = current.minute,
            is24Hour = is24Hour
        )
    }
    val title = stringResource(R.string.time_selection_dialog_title)
    val invalidTime = invalidTimeText(
        min = min?.let { IntakeMapper.localTimeToString(it) },
        max = max?.let { IntakeMapper.localTimeToString(it) },
        current = IntakeMapper.timeUnitsToString(hours = state.hour, minutes = state.minute)
    )
    val actionConfirm = stringResource(R.string.action_confirm)
    val actionCancel = stringResource(R.string.action_cancel)
    var badTime by remember { mutableStateOf(false) }
    LaunchedEffect(state.hour, state.minute) {
        badTime = false
    }
    TimePickerDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.padding(LocalDimensions.current.pagePadding),
        confirmButton = {
            AlertDialogDefaults.TonalElevation
            TextButton({
                val time = IntakeMapper.timeUnitsToString(
                    state.hour,
                    state.minute
                )
                val localTime = IntakeMapper.stringToLocalTime(time)

                val min = min ?: LocalTime.MIN
                val max = max ?: LocalTime.MAX

                if (localTime in min..max) {
                    onConfirm(time)
                } else {
                    badTime = true
                }

            }) {
                Text(actionConfirm)
            }
        },
        dismissButton = {
            TextButton(onDismissRequest) {
                Text(actionCancel)
            }
        },
        title = {
            Text(title, style = MaterialTheme.typography.titleLarge)
        },
        content = {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .verticalScroll(rememberScrollState())
                    .padding(top = LocalDimensions.current.defaultContentSpacing),
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.defaultContentSpacing)
            ) {
                TimePicker(
                    state = state,
                    modifier = Modifier
                )
                AnimatedVisibility(badTime) {
                    Text(
                        text = invalidTime,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun TimeSelectionDialogPreview() {
    val time = remember {
        LocalTime.now()
    }
    TimeSelectionDialog(
        min = null,
        max = time,
        current = time,
        onConfirm = {},
        onDismissRequest = {}
    )
}