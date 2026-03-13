package com.medicine.intake.tracker.domain.intake

interface IntakeWriter {
    suspend fun addIntake(intake: Intake)
    suspend fun updateIntake(intake: Intake)
}