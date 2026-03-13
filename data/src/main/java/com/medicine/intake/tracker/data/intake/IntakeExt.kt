package com.medicine.intake.tracker.data.intake

import com.medicine.intake.tracker.domain.intake.Intake

fun Intake.toEntity() = IntakeEntity(
    id = id,
    date = date,
    time = time,
    medicineId = medicineId
)

fun IntakeEntity.toDomain() = Intake(
    id = id,
    date = date,
    time = time,
    medicineId = medicineId
)