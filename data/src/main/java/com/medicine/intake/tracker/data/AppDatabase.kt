package com.medicine.intake.tracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medicine.intake.tracker.data.intake.IntakeDao
import com.medicine.intake.tracker.data.intake.IntakeEntity
import com.medicine.intake.tracker.data.medicine.MedicineDao
import com.medicine.intake.tracker.data.medicine.MedicineEntity

@Database(entities = [IntakeEntity::class, MedicineEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun intakeDao(): IntakeDao
    abstract fun medicineDao(): MedicineDao
}