package com.medicine.intake.tracker.data.medicine

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.MedicineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.collections.map
import kotlin.collections.single

private val Context.dataStore by preferencesDataStore("medicine")

private val CurrentMedicineIdKey = intPreferencesKey("current-medicine-id")

class AndroidMedicineRepository(
    context: Context,
    private val dao: MedicineDao
) : MedicineRepository {
    private val dataStore = context.dataStore
    override val medicines: Flow<List<Medicine>> = dao.getAll().map {
        it.map(MedicineEntity::toDomain)
    }
    override val currentMedicineId: Flow<MedicineId?> = dataStore.data.map {
        it[CurrentMedicineIdKey]
    }.combine(medicines) { currentMedicineId, medicines ->
        currentMedicineId ?: medicines.firstOrNull()?.id
    }

    override suspend fun updateCurrentMedicineId(medicineId: MedicineId?) {
        dataStore.edit { preferences ->
            medicineId?.let {
                preferences[CurrentMedicineIdKey] = it
            } ?: preferences.remove(CurrentMedicineIdKey)
        }
    }

    override suspend fun upsertMedicine(medicine: Medicine): Boolean {
        if (dao.getMedicineByName(medicine.name)?.id.let { it == null || it == medicine.id }) {
            dao.upsert(medicine.toEntity())

            val lastMedicines = medicines.first()
            if (lastMedicines.size == 1 && currentMedicineId.first() == null) {
                updateCurrentMedicineId(lastMedicines.single().id)
            }
            return true
        } else {
            return false
        }
    }

    override suspend fun deleteMedicine(medicine: Medicine) {
        dao.delete(medicine.toEntity())

        val lastCurrentMedicineId = currentMedicineId.first()
        val lastMedicines = medicines.first()

        if (lastCurrentMedicineId == medicine.id) {
            if (lastMedicines.size == 1) {
                updateCurrentMedicineId(lastMedicines.single().id)
            } else {
                updateCurrentMedicineId(null)
            }
        }
    }

    override suspend fun deleteAllMedicines() {
        dao.deleteAll()
        updateCurrentMedicineId(null)
    }
}