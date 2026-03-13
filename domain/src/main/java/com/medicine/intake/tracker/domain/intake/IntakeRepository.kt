package com.medicine.intake.tracker.domain.intake

interface IntakeRepository : IntakeProvider, IntakeDeleter, IntakeWriter {
    companion object {
        val classesForBind = arrayOf(
            IntakeRepository::class,
            IntakeProvider::class,
            IntakeDeleter::class,
            IntakeWriter::class
        )
    }
}