package com.medicine.intake.tracker.domain.intake.usecase

import com.medicine.intake.tracker.domain.intake.Intake
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.domain.medicine.Medicine
import java.time.Duration
import java.time.LocalDate

class AddIntakeUseCase {

    fun isIntakesLimit(
        currentMedicine: Medicine,
        recentIntakes: List<Intake>,
        gracePeriodHours: Int,
    ): Boolean {
        val localDateNow = LocalDate.now()
        val intakes = recentIntakes.filter {
            val sameDate = IntakeMapper.stringToLocalDate(it.date) == localDateNow
            val sameMedicine = it.medicineId == currentMedicine.id
            if (sameDate && sameMedicine) {
                val localDateTime = IntakeMapper.stringsToLocalDateTime(
                    it.date, it.time
                )
                val startOfDay = localDateNow.atStartOfDay()
                val duration = Duration.between(startOfDay, localDateTime)
                duration.toHours() > gracePeriodHours
            } else false
        }
        return intakes.size >= currentMedicine.intakesPerDay
    }

    fun addIntake(
        ignoreLimit: Boolean,
        currentMedicine: Medicine,
        recentIntakes: List<Intake>,
        gracePeriodHours: Int,
        onAdd: () -> Unit,
    ): Boolean {

        val isIntakesLimit = isIntakesLimit(
            currentMedicine = currentMedicine,
            recentIntakes = recentIntakes,
            gracePeriodHours = gracePeriodHours
        )
        return if (!isIntakesLimit || ignoreLimit) {
            onAdd()
            true
        } else false
    }
}