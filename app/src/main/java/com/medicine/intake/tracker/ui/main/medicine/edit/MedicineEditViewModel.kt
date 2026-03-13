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

class MedicineEditViewModel(
    private val repository: MedicineRepository
) : ViewModel() {
    private val _medicineToEditId = MutableStateFlow<MedicineId?>(null)

    val state = combine(
        _medicineToEditId,
        repository.medicines
    ) { id, medicines ->
        if (id == null) {
            return@combine MedicineEditUiState.Loading
        }
        MedicineEditUiState.Edit(medicines.find { id == it.id })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MedicineEditUiState.Loading)

    fun loadMedicine(id: MedicineId) {
        _medicineToEditId.value = id
    }

    fun setUpToCreateMedicine() {
        _medicineToEditId.value = -1
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