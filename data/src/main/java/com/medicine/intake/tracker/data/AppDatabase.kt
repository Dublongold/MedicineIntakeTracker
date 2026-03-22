package com.medicine.intake.tracker.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.medicine.intake.tracker.data.intake.IntakeDao
import com.medicine.intake.tracker.data.intake.IntakeEntity
import com.medicine.intake.tracker.data.medicine.MedicineDao
import com.medicine.intake.tracker.data.medicine.MedicineEntity

@Database(
    version = 2,
    entities = [IntakeEntity::class, MedicineEntity::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun intakeDao(): IntakeDao
    abstract fun medicineDao(): MedicineDao
}