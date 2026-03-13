package com.medicine.intake.tracker.ui.main.medicine.edit

sealed interface MedicineActionResult {
    object Success : MedicineActionResult
    object SameName : MedicineActionResult
}