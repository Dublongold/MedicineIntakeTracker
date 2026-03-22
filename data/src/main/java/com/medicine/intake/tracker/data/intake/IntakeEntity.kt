package com.medicine.intake.tracker.data.intake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.medicine.intake.tracker.data.medicine.MedicineEntity
import com.medicine.intake.tracker.domain.intake.IntakeId
import com.medicine.intake.tracker.domain.medicine.MedicineId

@Entity(
    tableName = "intake",
    foreignKeys = [ForeignKey(
        entity = MedicineEntity::class,
        parentColumns = ["id"],
        childColumns = ["medicineId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["medicineId"]), Index(
        value = ["medicineId", "date", "time"],
        unique = true
    )]
)
data class IntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: IntakeId = 0,
    val date: String,
    val time: String,
    val medicineId: MedicineId,
)