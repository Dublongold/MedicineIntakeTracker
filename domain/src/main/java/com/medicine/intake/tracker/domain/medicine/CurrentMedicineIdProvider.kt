package com.medicine.intake.tracker.domain.medicine

import kotlinx.coroutines.flow.Flow

interface CurrentMedicineIdProvider {
    val currentMedicineId: Flow<MedicineId?>
}