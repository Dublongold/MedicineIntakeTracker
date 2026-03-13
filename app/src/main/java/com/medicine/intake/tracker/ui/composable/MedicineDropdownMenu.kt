package com.medicine.intake.tracker.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.createFakeMedicines
import com.medicine.intake.tracker.ui.main.medicine.contentDescription
import com.medicine.intake.tracker.ui.main.medicine.painter
import com.medicine.intake.tracker.ui.theme.LocalDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDropdownMenu(
    selectedId: MedicineId?,
    values: List<Medicine>,
    onSelected: (MedicineId) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        val selected = values.find { it.id == selectedId }
        TextField(
            value = selected?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.medicine_dropdown_menu_label)) },
            leadingIcon = selected?.icon?.let {
                {
                    Icon(it.painter, it.contentDescription, Modifier.size(32.dp))
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
        )
        ExposedDropdownMenu(expanded, { expanded = false }) {
            values.forEach { medicine ->
                DropdownMenuItem(
                    text = {
                        Text(text = medicine.name)
                    },
                    onClick = {
                        onSelected(medicine.id)
                        expanded = false
                    },
                    leadingIcon = medicine.icon?.let { icon ->
                        {
                            Icon(
                                icon.painter,
                                icon.contentDescription,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun MedicineDropdownMenuPreview() {
    val medicines = remember { createFakeMedicines() }
    var selectedId by remember { mutableStateOf<MedicineId?>(null) }
    Scaffold {
        Box(Modifier
            .fillMaxSize()
            .padding(it)
            .padding(LocalDimensions.current.pagePadding)) {
            MedicineDropdownMenu(
                selectedId = selectedId,
                values = medicines,
                onSelected = { selectedId = it }
            )
        }
    }
}