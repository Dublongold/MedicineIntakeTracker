package com.medicine.intake.tracker.data.medicine

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.medicine.intake.tracker.domain.medicine.Medicine
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicine")
    fun getAll(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicine where id = :id")
    fun getById(id: Int): Flow<MedicineEntity>

    @Upsert
    suspend fun upsert(medicine: MedicineEntity): Long

    @Delete
    suspend fun delete(medicine: MedicineEntity)

    @Query("DELETE FROM medicine")
    suspend fun deleteAll()

    @Query("SELECT * FROM medicine WHERE name = :name COLLATE NOCASE")
    suspend fun getMedicineByName(name: String): MedicineEntity?
}