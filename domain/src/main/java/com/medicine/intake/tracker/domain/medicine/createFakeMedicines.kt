package com.medicine.intake.tracker.domain.medicine

fun createFakeMedicines() = listOf(
    Medicine(
        id = 1,
        name = "Amoxicillin",
        description = "Antibiotic for bacterial infections",
        intakesPerDay = 3,
        icon = MedicineIcon.BlisterPills
    ),
    Medicine(
        id = 2,
        name = "Lisinopril",
        description = "High blood pressure medication",
        intakesPerDay = 1,
        icon = MedicineIcon.Tablet
    ),
    Medicine(
        id = 3,
        name = "Metformin",
        description = "Management of Type 2 diabetes",
        intakesPerDay = 2,
        icon = MedicineIcon.Tablet
    ),
    Medicine(
        id = 4,
        name = "Atorvastatin",
        description = "Used to lower cholesterol",
        intakesPerDay = 1,
        icon = MedicineIcon.Tablet
    ),
    Medicine(
        id = 5,
        name = "Lantus",
        description = "Long-acting insulin injection",
        intakesPerDay = 1,
        icon = MedicineIcon.Syringe
    ),
    Medicine(
        id = 6,
        name = "Ibuprofen",
        description = "Relief for pain and inflammation",
        intakesPerDay = 3,
        icon = MedicineIcon.BlisterPills
    ),
    Medicine(
        id = 7,
        name = "Robitussin",
        description = "Relief for chest congestion",
        intakesPerDay = 4,
        icon = MedicineIcon.Bottle
    ),
    Medicine(
        id = 8,
        name = "Hydrocortisone",
        description = "Topical cream for skin rashes",
        intakesPerDay = 2,
        icon = MedicineIcon.BandageAdhesive
    ),
    Medicine(
        id = 9,
        name = "Omeprazole",
        description = "Treatment for frequent heartburn",
        intakesPerDay = 1,
        icon = MedicineIcon.BlisterPills
    ),
    Medicine(
        id = 10,
        name = "Multivitamin",
        description = "Daily dietary supplement",
        intakesPerDay = 1,
        icon = MedicineIcon.Bottle
    )
)