package com.medicine.intake.tracker.domain.medicine

interface MedicineRepository : MedicineProvider, MedicineDeleter, CurrentMedicineIdUpdater,
    MedicineWriter {
    companion object {
        val classesForBind = arrayOf(
            MedicineRepository::class,
            *MedicineProvider.classesForBind,
            MedicineDeleter::class,
            CurrentMedicineIdUpdater::class,
            MedicineWriter::class
        )
    }
}