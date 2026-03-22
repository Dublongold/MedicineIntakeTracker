package com.medicine.intake.tracker.ui.main.home.dialog.customintake

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R

/**
 * Displays a text field with increment and decrement buttons for a value, that can be
 * incremented and decremented.
 * For now, it's used for a date to modify it by days in [AddCustomIntakeDialog].
 */
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
private fun IntegerOptionPreview() {
    var value by remember { mutableIntStateOf(0) }
    IntegerOption(
        content = value.toString(),
        label = { Text("Label") },
        onIncrement = { value++ },
        onDecrement = { value-- }
    )
}