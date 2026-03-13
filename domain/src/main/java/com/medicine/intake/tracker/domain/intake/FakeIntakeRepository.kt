package com.medicine.intake.tracker.domain.intake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeIntakeRepository(
    initialIntakes: List<Intake> = emptyList()
) : IntakeRepository {

    companion object {
        fun withFakeData(): FakeIntakeRepository {
            return FakeIntakeRepository(createFakeIntakes())
        }
    }

    private val _intakes = MutableStateFlow(initialIntakes)
    override val sortedIntakes = _intakes.asStateFlow()

    override suspend fun addIntake(intake: Intake) {
        _intakes.update { currentList ->
            currentList + intake
        }
    }

    override suspend fun updateIntake(intake: Intake) {
        _intakes.update { currentList ->
            currentList.map {
                if (it.id == intake.id) intake else it
            }
        }
    }

    override suspend fun deleteIntake(intake: Intake) {
        _intakes.update { currentList ->
            currentList.filterNot { it.id == intake.id }
        }
    }

    override suspend fun deleteAllIntakes() {
        _intakes.value = emptyList()
    }
}