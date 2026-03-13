package com.medicine.intake.tracker.domain.medicine

interface CurrentMedicineIdUpdater {
    suspend fun updateCurrentMedicineId(medicineId: MedicineId?)
}