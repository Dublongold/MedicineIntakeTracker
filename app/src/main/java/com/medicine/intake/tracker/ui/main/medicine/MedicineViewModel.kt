package com.medicine.intake.tracker.ui.main.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicine.intake.tracker.domain.medicine.CurrentMedicineIdUpdater
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineDeleter
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.MedicineProvider
import com.medicine.intake.tracker.domain.medicine.MedicineWriter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicineViewModel(
    medicineProvider: MedicineProvider,
    private val currentMedicineIdUpdater: CurrentMedicineIdUpdater,
    private val medicineDeleter: MedicineDeleter,
    private val medicineWriter: MedicineWriter
) : ViewModel() {
    val state = combine(
        medicineProvider.currentMedicineId,
        medicineProvider.medicines,
    ) { currentMedicineId, medicines ->
        MedicineUiState.Loaded(currentMedicineId, medicines)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MedicineUiState.Loading)

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            medicineDeleter.deleteMedicine(medicine)
        }
    }

    fun updateCurrentMedicineId(id: MedicineId) {
        viewModelScope.launch {
            currentMedicineIdUpdater.updateCurrentMedicineId(id)
        }
    }

    fun updateMedicineCompleted(medicine: Medicine, isCompleted: Boolean) {
        viewModelScope.launch {
            medicineWriter.upsertMedicine(
                medicine.copy(
                    isCompleted = isCompleted
                )
            )
        }
    }

//    fun debugAddRandomMedicine() {
//        viewModelScope.launch {
//            createFakeMedicines().random().let {
//                repository.upsertMedicine(it.copy(id = 0))
//            }
//        }
//    }
}