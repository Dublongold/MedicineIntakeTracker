package com.medicine.intake.tracker.ui.main.medicine.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicine.intake.tracker.domain.medicine.Medicine
import com.medicine.intake.tracker.domain.medicine.MedicineId
import com.medicine.intake.tracker.domain.medicine.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * First value: An id of the medicine to edit, null if creating a new one.
 * Second value: True if loaded, false if not.
 */
class MedicineEditViewModel(
    private val repository: MedicineRepository
) : ViewModel() {
    private val _medicineToEditId = MutableStateFlow<MedicineIdToEdit?>(null)

    val state = combine(
        _medicineToEditId,
        repository.medicines
    ) { medicineIdToEdit, medicines ->
        if (medicineIdToEdit == null) {
            return@combine MedicineEditUiState.Loading
        }

        MedicineEditUiState.Edit(medicines.find { medicineIdToEdit.id == it.id })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MedicineEditUiState.Loading)

    fun loadMedicine(id: MedicineId?) {
        _medicineToEditId.value = MedicineIdToEdit(id)
    }

    fun editOrAddMedicine(
        medicine: Medicine,
        onComplete: (MedicineActionResult) -> Unit
    ) {
        viewModelScope.launch {
            if (!repository.upsertMedicine(medicine)) {
                onComplete(MedicineActionResult.SameName)
            } else {
                onComplete(MedicineActionResult.Success)
            }
        }
    }



        fun deleteMedicine(medicine: Medicine) {
            viewModelScope.launch {
                repository.deleteMedicine(medicine)
            }
        }
    }