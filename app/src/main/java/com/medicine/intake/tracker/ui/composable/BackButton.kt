package com.medicine.intake.tracker.ui.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.medicine.intake.tracker.R

@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    IconButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        Icon(painterResource(R.drawable.ic_arrow_back), "Назад")
    }
}

@Preview
@Composable
private fun BackButtonPreview() {
    BackButton({})
}