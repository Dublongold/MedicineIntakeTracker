package com.medicine.intake.tracker.domain.intake

fun createFakeIntakes() = listOf(
    // Amoxicillin (ID 1) - 4 total intakes (3 per day max)
    Intake(id = 1, date = "2026-03-03", time = "08:00", medicineId = 1),
    Intake(id = 2, date = "2026-03-03", time = "14:00", medicineId = 1),
    Intake(id = 3, date = "2026-03-03", time = "22:00", medicineId = 1),
    Intake(id = 4, date = "2026-03-04", time = "08:00", medicineId = 1),
    Intake(id = 5, date = "2026-03-04", time = "14:00", medicineId = 1),
    Intake(id = 6, date = "2026-03-04", time = "22:00", medicineId = 1),
    Intake(id = 7, date = "2026-03-05", time = "08:00", medicineId = 1),
    Intake(id = 8, date = "2026-03-06", time = "08:00", medicineId = 1),

    // Lisinopril (ID 2) - 3 total intakes (1 per day)
    Intake(id = 5, date = "2026-03-04", time = "09:00", medicineId = 2),
    Intake(id = 6, date = "2026-03-06", time = "09:00", medicineId = 2),
//    Intake(id = 7, date = "2026-03-07", time = "09:00", medicineId = 2),

    // Metformin (ID 3) - 4 total intakes (2 per day)
    Intake(id = 8, date = "2026-03-04", time = "08:30", medicineId = 3),
    Intake(id = 9, date = "2026-03-04", time = "20:30", medicineId = 3),
    Intake(id = 10, date = "2026-03-05", time = "08:30", medicineId = 3),
    Intake(id = 11, date = "2026-03-05", time = "20:30", medicineId = 3),

    // Atorvastatin (ID 4) - 3 total intakes (1 per day)
    Intake(id = 12, date = "2026-03-04", time = "21:00", medicineId = 4),
    Intake(id = 13, date = "2026-03-05", time = "21:00", medicineId = 4),
    Intake(id = 14, date = "2026-03-06", time = "21:00", medicineId = 4),

    // Lantus (ID 5) - 3 total intakes (1 per day)
    Intake(id = 15, date = "2026-03-04", time = "07:00", medicineId = 5),
    Intake(id = 16, date = "2026-03-05", time = "07:00", medicineId = 5),
    Intake(id = 17, date = "2026-03-07", time = "07:00", medicineId = 5)
).sortedWith(compareByDescending<Intake> { it.date }.thenByDescending { it.time }
    .thenByDescending { it.medicineId })