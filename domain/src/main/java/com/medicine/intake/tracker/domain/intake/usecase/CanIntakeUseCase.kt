package com.medicine.intake.tracker.domain.intake.usecase

import com.medicine.intake.tracker.domain.datetime.CurrentLocalDateTimeProvider
import com.medicine.intake.tracker.domain.intake.IntakeProvider
import com.medicine.intake.tracker.domain.medicine.CurrentMedicineIdProvider
import kotlinx.coroutines.flow.combine
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

private const val TWO_HOURS_MS = 2 * 60 * 60 * 1000L

class CanIntakeUseCase(
    currentLocalDateTimeProvider: CurrentLocalDateTimeProvider,
    currentMedicineIdProvider: CurrentMedicineIdProvider,
    intakeProvider: IntakeProvider,
) {
    val canIntake = combine(
        currentLocalDateTimeProvider.localDateTime,
        currentMedicineIdProvider.currentMedicineId,
        intakeProvider.sortedIntakes
    ) { currentLocalDateTime, medicineId, intakes ->
        val intakesByCurrentMedicineId = intakes.filter {
            it.medicineId == medicineId
        }

        val lastIntake = intakesByCurrentMedicineId.maxByOrNull {
            it.date + it.time
        }

        return@combine if (lastIntake == null) {
            true
        } else {
            val lastIntakeTime = LocalDate.parse(lastIntake.date).atTime(
                LocalTime.parse(lastIntake.time)
            )
            val duration = Duration.between(lastIntakeTime, currentLocalDateTime)

            duration.toMillis() >= TWO_HOURS_MS
        }
    }
}