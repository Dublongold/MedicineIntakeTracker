package com.medicine.intake.tracker.ui.main.medicine

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.medicine.intake.tracker.R
import com.medicine.intake.tracker.domain.medicine.MedicineIcon


val MedicineIcon.painter
    @Composable get() = painterResource(
        when (this) {
            MedicineIcon.Tablet -> R.drawable.ic_medicine_tablet
            MedicineIcon.BandageAdhesive -> R.drawable.ic_medicine_bandage_adhesive
            MedicineIcon.Syringe -> R.drawable.ic_medicine_syringe
            MedicineIcon.BlisterPills -> R.drawable.ic_medicine_blister_pills
            MedicineIcon.Bottle -> R.drawable.ic_medicine_bottle
        }
    )

val MedicineIcon.contentDescription
    @Composable get() = "" // TODO: Add content description