package com.medicine.intake.tracker.data.intake

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.IntakeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class AndroidIntakeRepository(
    private val intakeDao: IntakeDao
) : IntakeRepository {
    override val sortedIntakes: Flow<List<Intake>> = intakeDao.getAll().map {
        it.map(IntakeEntity::toDomain)
    }

    override suspend fun deleteIntake(intake: Intake) = intakeDao.delete(intake.toEntity())

    override suspend fun deleteAllIntakes() = intakeDao.deleteAll()

    override suspend fun addIntake(intake: Intake) = intakeDao.upsert(intake.toEntity())

    override suspend fun updateIntake(intake: Intake) = intakeDao.upsert(intake.toEntity())
}