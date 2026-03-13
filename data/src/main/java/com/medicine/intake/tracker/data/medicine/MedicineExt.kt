package com.medicine.intake.tracker.data.medicine

import com.medicine.intake.tracker.domain.medicine.Medicine

fun MedicineEntity.toDomain(): Medicine = Medicine(
    id = id,
    name = name,
    description = description,
    intakesPerDay = intakesPerDay,
    icon = icon
)

fun Medicine.toEntity(): MedicineEntity = MedicineEntity(
    id = id,
    name = name,
    description = description,
    intakesPerDay = intakesPerDay,
    icon = icon
)