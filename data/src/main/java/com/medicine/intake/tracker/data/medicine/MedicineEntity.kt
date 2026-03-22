package com.medicine.intake.tracker.data.medicine

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.medicine.intake.tracker.data.mapper.MedicineTypeConverters
import com.medicine.intake.tracker.domain.medicine.MedicineIcon
import com.medicine.intake.tracker.domain.medicine.MedicineId

@Entity(tableName = "medicine", indices = [Index(value = ["name"], unique = true)])
@TypeConverters(MedicineTypeConverters::class)
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true) val id: MedicineId = 0,
    val name: String,
    val description: String?,
    val intakesPerDay: Int,
    val icon: MedicineIcon?,
    @ColumnInfo(defaultValue = "false")
    val isCompleted: Boolean,
)

