package com.medicine.intake.tracker.data.intake

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.medicine.intake.tracker.domain.medicine.MedicineId
import kotlinx.coroutines.flow.Flow

@Dao
interface IntakeDao {
    @Query("SELECT * FROM intake ORDER BY date DESC, time DESC")
    fun getAll(): Flow<List<IntakeEntity>>

    @Upsert
    suspend fun upsert(intake: IntakeEntity)

    @Delete
    suspend fun delete(intake: IntakeEntity)

    @Query("DELETE FROM intake")
    suspend fun deleteAll()
}