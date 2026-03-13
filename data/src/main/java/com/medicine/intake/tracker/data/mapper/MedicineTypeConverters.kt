package com.medicine.intake.tracker.data.mapper

import androidx.room.TypeConverter
import com.medicine.intake.tracker.domain.medicine.MedicineIcon

class MedicineTypeConverters {
    @TypeConverter
    fun medicineIconToString(medicineIcon: MedicineIcon) = medicineIcon.id
    @TypeConverter
    fun idToMedicineIcon(id: Int) = MedicineIcon.entries.first { it.id == id }
}