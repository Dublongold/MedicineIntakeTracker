package com.medicine.intake.tracker.domain.medicine

interface MedicineWriter {
    suspend fun upsertMedicine(medicine: Medicine): Boolean
}