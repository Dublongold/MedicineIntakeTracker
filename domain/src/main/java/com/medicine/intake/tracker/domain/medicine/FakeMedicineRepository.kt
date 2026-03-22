package com.medicine.intake.tracker.domain.medicine

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

private const val RANDOM_CURRENT_MEDICINE_ID: MedicineId = -1

class FakeMedicineRepository(
    initialCurrentMedicineId: MedicineId? = null, initialMedicines: List<Medicine> = mutableListOf()
) : MedicineRepository {
    companion object {
        fun withFakeData(
            initialCurrentMedicineId: MedicineId? = RANDOM_CURRENT_MEDICINE_ID
        ): FakeMedicineRepository {
            val medicines = createFakeMedicines()
            return FakeMedicineRepository(
                initialCurrentMedicineId = initialCurrentMedicineId?.let {
                    if (it == RANDOM_CURRENT_MEDICINE_ID) {
                        medicines.random().id
                    } else it
                }, initialMedicines = medicines
            )
        }
    }

    private val _medicines = MutableStateFlow(initialMedicines)
    override val medicines = _medicines.asStateFlow()

    private val _currentMedicineId = MutableStateFlow(initialCurrentMedicineId)
    override val currentMedicineId =
        _currentMedicineId.asStateFlow().combine(medicines) { currentId, medicines ->
            currentId ?: medicines.firstOrNull()?.id
        }

    override suspend fun updateCurrentMedicineId(medicineId: MedicineId?) {
        _currentMedicineId.value = medicineId
    }

    override suspend fun upsertMedicine(medicine: Medicine): Boolean {
        val tryingToAddWithExistingName = _medicines.value.any {
            (medicine.id == 0 || it.id != medicine.id) && it.name.equals(
                medicine.name,
                ignoreCase = true
            )
        }
        if (tryingToAddWithExistingName) {
            return false
        }
        _medicines.update { medicines ->
            if (medicines.none { it.id == medicine.id }) {
                medicines + medicine
            } else {
                medicines.map {
                    if (it.id == medicine.id) medicine else it
                }
            }
        }
        if (_medicines.value.size == 1) {
            updateCurrentMedicineId(medicine.id)
        }
        return true
    }

    override suspend fun deleteMedicine(medicine: Medicine) {
        _medicines.update {
            it.filter { currentMedicine ->
                currentMedicine.id != medicine.id
            }
        }
        if (_currentMedicineId.value == medicine.id) {
            if (_medicines.value.size == 1) {
                updateCurrentMedicineId(_medicines.value.single().id)
            } else {
                updateCurrentMedicineId(null)
            }
        }
    }

    override suspend fun deleteAllMedicines() {
        _medicines.update {
            emptyList()
        }
        updateCurrentMedicineId(null)
    }
}