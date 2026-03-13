package com.medicine.intake.tracker.domain.intake

import kotlinx.coroutines.flow.Flow

interface IntakeProvider {
    val sortedIntakes: Flow<List<Intake>>
}