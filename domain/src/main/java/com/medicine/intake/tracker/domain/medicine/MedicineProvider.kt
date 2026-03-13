package com.medicine.intake.tracker.domain.medicine

import kotlinx.coroutines.flow.Flow

interface MedicineProvider: CurrentMedicineIdProvider {
    val medicines: Flow<List<Medicine>>
    companion object {
        val classesForBind = arrayOf(
            MedicineProvider::class,
            CurrentMedicineIdProvider::class
        )
    }
}