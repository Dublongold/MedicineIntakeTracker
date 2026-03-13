package com.medicine.intake.tracker.ui.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.ui.composable.TimeSelectionDialog
import com.medicine.intake.tracker.ui.composable.invalidTimeText
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

private val CharsToReplaceRegex = Regex("""[.,;-]""")

private enum class BadTimeVariant {
    InvalidFormat,
    OutOfRange;
}

@Composable
fun AddCustomIntakeDialog(
    minDateTime: LocalDateTime?,
    onAdd: (String, String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val maxDateTime = remember {
        LocalDateTime.now().withSecond(0).withNano(0)
    }
    val title = stringResource(R.string.home_add_custom_intake_dialog_title)
    val actionAdd = stringResource(R.string.action_add)
    val actionCancel = stringResource(R.string.action_cancel)
    val badTimeSupportingText = stringResource(R.string.home_add_custom_intake_dialog_bad_time)

    var currentLocalDateTime by remember(maxDateTime) {
        mutableStateOf(maxDateTime)
    }
    val date = IntakeMapper.localDateToString(currentLocalDateTime.toLocalDate())
    var time by remember(currentLocalDateTime) {
        mutableStateOf(
            IntakeMapper.localTimeToString(
                currentLocalDateTime.toLocalTime()
            )
        )
    }
    val timeOutOfRangeSupportingText = invalidTimeText(
        min = minDateTime?.takeIf { it.toLocalDate() == currentLocalDateTime.toLocalDate() }
            ?.let { IntakeMapper.localTimeToString(it.toLocalTime()) },
        max = IntakeMapper.localTimeToString(maxDateTime.toLocalTime()),
        current = time
    )
    var showTimeSelectionDialog by remember {
        mutableStateOf(false)
    }
    var badTime by remember { mutableStateOf<BadTimeVariant?>(null) }
    val dateLabel = stringResource(R.string.home_add_custom_intake_dialog_date_label)
    val timeLabel = stringResource(R.string.home_add_custom_intake_dialog_time_label)
    AlertDialog(
        onDismissRequest,
        modifier = modifier.padding(LocalDimensions.current.pagePadding),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        title = {
            Text(title)
        },
        text = {
            Column {
                IntegerOption(
                    content = date,
                    label = { Text(dateLabel) },
                    enabled = run {
                        val a = currentLocalDateTime.toLocalDate()
                        val b = minDateTime?.toLocalDate() ?: maxDateTime.minusDays(7).toLocalDate()
                        val c = maxDateTime.toLocalDate()
                        (a > b) to (a < c)
                    },
                    onIncrement = {
                        val incremented = currentLocalDateTime.plusDays(1)
                        if (incremented <= maxDateTime) {
                            currentLocalDateTime = incremented
                        }
                    },
                    onDecrement = {
                        currentLocalDateTime = currentLocalDateTime.minusDays(1)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = time,
                    label = { Text(timeLabel) },
                    onValueChange = {
                        badTime = null
                        time = it.replace(CharsToReplaceRegex, ":")
                    },
                    isError = badTime != null,
                    supportingText = if (badTime != null) {
                        {
                            Text(
                                if (badTime == BadTimeVariant.InvalidFormat) {
                                    badTimeSupportingText
                                } else {
                                    timeOutOfRangeSupportingText
                                }
                            )
                        }
                    } else null,
                    trailingIcon = {
                        IconButton({
                            showTimeSelectionDialog = true
                        }) {
                            Icon(
                                painterResource(R.drawable.ic_clock),
                                null,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton({
                val localTime = try {
                    IntakeMapper.stringToLocalTime(time)
                } catch (_: DateTimeParseException) {
                    null
                }
                if (localTime == null) {
                    badTime = BadTimeVariant.InvalidFormat
                } else {
                    val localDate = currentLocalDateTime.toLocalDate()
                    val maxDate = maxDateTime.toLocalDate()
                    val minDate = minDateTime?.toLocalDate() ?: LocalDate.MIN

                    if (localDate == maxDate) {
                        val maxTime = maxDateTime.toLocalTime()
                        if (localTime > maxTime) {
                            badTime = BadTimeVariant.OutOfRange
                            return@TextButton
                        }
                    } else if (localDate == minDate) {
                        val minTime = minDateTime?.toLocalTime() ?: LocalTime.MIN
                        if (localTime < minTime) {
                            badTime = BadTimeVariant.OutOfRange
                            return@TextButton
                        }
                    }

                    onAdd(date, time)
                }

            }) {
                Text(actionAdd)
            }
        },
        dismissButton = {
            TextButton(onDismissRequest) {
                Text(actionCancel)
            }
        }
    )
    if (showTimeSelectionDialog) {
        val localTime = remember(maxDateTime) {
            maxDateTime.toLocalTime()
        }
        TimeSelectionDialog(
            min = minDateTime?.takeIf {
                it.toLocalDate() == currentLocalDateTime.toLocalDate()
            }?.toLocalTime(),
            max = maxDateTime.takeIf {
                it.toLocalDate() == currentLocalDateTime.toLocalDate()
            }?.toLocalTime(),
            current = localTime,
            onConfirm = {
                time = it
                showTimeSelectionDialog = false
            },
            onDismissRequest = {
                showTimeSelectionDialog = false
            }
        )
    }
}

@Composable
fun IntegerOption(
    content: String,
    label: @Composable () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Pair<Boolean, Boolean> = Pair(true, true)
) {
    OutlinedTextField(
        value = content,
        leadingIcon = {
            IconButton(onDecrement, enabled = enabled.first) {
                Icon(
                    painterResource(R.drawable.ic_minus),
                    null,
                )
            }
        },
        trailingIcon = {
            IconButton(onIncrement, enabled = enabled.second) {
                Icon(
                    painterResource(R.drawable.ic_add),
                    null,
                )
            }
        },
        onValueChange = {},
        label = label,
        readOnly = true,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun AddCustomIntakeDialogPreview() {
    AddCustomIntakeDialog(
        minDateTime = remember {
            LocalDateTime.now().minusHours(6).withSecond(0).withNano(0).takeIf {
                LocalDate.now() == it.toLocalDate()
            }
        },
        onAdd = { _, _ -> },
        onDismissRequest = {}
    )
}