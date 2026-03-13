package com.medicine.intake.tracker.domain.medicine

interface MedicineDeleter {
    suspend fun deleteMedicine(medicine: Medicine)
    suspend fun deleteAllMedicines()
}