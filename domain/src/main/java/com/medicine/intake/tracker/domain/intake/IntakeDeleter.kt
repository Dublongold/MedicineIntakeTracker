package com.medicine.intake.tracker.domain.intake

interface IntakeDeleter {
    suspend fun deleteIntake(intake: Intake)
    suspend fun deleteAllIntakes()
}