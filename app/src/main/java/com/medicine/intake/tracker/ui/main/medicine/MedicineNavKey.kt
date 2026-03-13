package com.medicine.intake.tracker.ui.main.medicine

import androidx.navigation3.runtime.NavKey
import com.medicine.intake.tracker.domain.medicine.MedicineId
import kotlinx.serialization.Serializable

sealed interface MedicineNavKey : NavKey {
    @Serializable
    object Main : MedicineNavKey
    @Serializable
    data class Edit(
        val medicineId: MedicineId?,
    ) : MedicineNavKey
}