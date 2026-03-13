package com.medicine.intake.tracker.domain.intake

import com.medicine.intake.tracker.domain.medicine.MedicineId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Intake(
    val id: Int = 0,
    val date: String,
    val time: String,
    val medicineId: MedicineId,
) {
    constructor(id: Int = 0, localDate: LocalDate, localTime: LocalTime, medicineId: MedicineId) : this(
        id = id,
        date = IntakeMapper.localDateToString(localDate),
        time = IntakeMapper.localTimeToString(localTime),
        medicineId = medicineId
    )

    constructor(id: Int = 0, localDateTime: LocalDateTime, medicineId: MedicineId) : this(
        id = id,
        localDate = localDateTime.toLocalDate(),
        localTime = localDateTime.toLocalTime(),
        medicineId = medicineId
    )
}