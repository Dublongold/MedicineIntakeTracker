package com.medicine.intake.tracker.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@Composable
fun VerticalArrows(
    onClickUp: () -> Unit,
    onClickDown: () -> Unit,
    modifier: Modifier = Modifier,
    upEnabled: Boolean = true,
    downEnabled: Boolean = true,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClickUp, enabled = upEnabled) {
            Icon(painterResource(R.drawable.ic_keyboard_arrow_up), "Up arrow")
        }
        VerticalDivider(
            modifier = Modifier.weight(1f)
        )
        IconButton(onClickDown, enabled = downEnabled) {
            Icon(painterResource(R.drawable.ic_keyboard_arrow_down), "Down arrow")
        }
    }
}

@Preview
@Composable
private fun VerticalArrowsPreview() {
    VerticalArrows(
        onClickUp = {},
        onClickDown = {}
    )
}