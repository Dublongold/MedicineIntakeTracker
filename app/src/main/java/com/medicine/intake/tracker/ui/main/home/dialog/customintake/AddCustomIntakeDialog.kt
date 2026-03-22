package com.medicine.intake.tracker.ui.main.home.dialog.customintake

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.ui.composable.dialog.TimeSelectionDialog
import com.medicine.intake.tracker.ui.composable.invalidTimeText
import com.medicine.intake.tracker.ui.theme.LocalDimensions
import com.medicine.intake.tracker.util.rememberLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime

private val CharsToReplaceRegex = Regex("""[.,;-]""")

@Composable
fun AddCustomIntakeDialog(
    minDateTime: LocalDateTime?,
    onAdd: (String, String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val maxDateTime = rememberLocalDateTime(atStartOfMinute = true)
    var localDate by remember {
        mutableStateOf(maxDateTime.toLocalDate())
    }
    val date = IntakeMapper.localDateToString(localDate)
    var time by remember {
        mutableStateOf(
            IntakeMapper.localTimeToString(
                maxDateTime.toLocalTime()
            )
        )
    }
    var showTimeSelectionDialog by remember {
        mutableStateOf(false)
    }
    var badTime by remember { mutableStateOf<BadCustomTimeVariant?>(null) }
    AlertDialog(
        onDismissRequest,
        modifier = modifier.padding(LocalDimensions.current.pagePadding),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        title = {
            Text(stringResource(R.string.home_add_custom_intake_dialog_title))
        },
        text = {
            Column {
                IntegerOption(
                    content = date,
                    label = { Text(stringResource(R.string.home_add_custom_intake_dialog_date_label)) },
                    enabled = run {
                        val a = localDate
                        val b = (minDateTime ?: maxDateTime.minusDays(7)).toLocalDate()
                        val c = maxDateTime.toLocalDate()
                        (a > b) to (a < c)
                    },
                    onIncrement = {
                        val incremented = localDate.plusDays(1)
                        if (incremented <= maxDateTime.toLocalDate()) {
                            localDate = incremented
                        }
                    },
                    onDecrement = {
                        localDate = localDate.minusDays(1)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = time,
                    label = { Text(stringResource(R.string.home_add_custom_intake_dialog_time_label)) },
                    onValueChange = {
                        badTime = null
                        time = it.replace(CharsToReplaceRegex, ":")
                    },
                    isError = badTime != null,
                    supportingText = if (badTime != null) {
                        {
                            Text(
                                if (badTime == BadCustomTimeVariant.InvalidFormat) {
                                    stringResource(R.string.home_add_custom_intake_dialog_bad_time)
                                } else {
                                    invalidTimeText(
                                        min = minDateTime?.takeIf { it.toLocalDate() == localDate }
                                            ?.let { IntakeMapper.localTimeToString(it.toLocalTime()) },
                                        max = IntakeMapper.localTimeToString(maxDateTime.toLocalTime()),
                                        current = time
                                    )
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
                val result = validateAddCustomIntakeTime(
                    time = time,
                    localDate = localDate,
                    minDateTime = minDateTime,
                    maxDateTime = maxDateTime
                )
                when (result) {
                    is AddCustomIntakeTimeValidationResult.BadCustomTimeTime -> {
                        badTime = result.variant
                    }

                    AddCustomIntakeTimeValidationResult.Success -> {
                        onAdd(date, time)
                    }
                }
            }) {
                Text(stringResource(R.string.action_add))
            }
        },
        dismissButton = {
            TextButton(onDismissRequest) {
                Text(stringResource(R.string.action_cancel))
            }
        })
    if (showTimeSelectionDialog) {
        val localTime = maxDateTime.toLocalTime()
        TimeSelectionDialog(min = minDateTime?.takeIf {
            it.toLocalDate() == localDate
        }?.toLocalTime(), max = maxDateTime.takeIf {
            it.toLocalDate() == localDate
        }?.toLocalTime(), current = localTime, onConfirm = {
            time = it
            showTimeSelectionDialog = false
        }, onDismissRequest = {
            showTimeSelectionDialog = false
        })
    }
}


@Preview
@Composable
private fun AddCustomIntakeDialogPreview() {
    AddCustomIntakeDialog(minDateTime = remember {
        LocalDateTime.now().minusHours(6).withSecond(0).withNano(0).takeIf {
            LocalDate.now() == it.toLocalDate()
        }
    }, onAdd = { _, _ -> }, onDismissRequest = {})
}