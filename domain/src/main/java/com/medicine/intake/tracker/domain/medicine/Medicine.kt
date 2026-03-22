package com.medicine.intake.tracker.domain.medicine

data class Medicine(
    val id: MedicineId,
    val name: String,
    val description: String?,
    val intakesPerDay: Int,
    val icon: MedicineIcon?,
    val isCompleted: Boolean = false,
)