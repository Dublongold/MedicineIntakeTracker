package com.medicine.intake.tracker.domain.intake.usecase

import com.medicine.intake.tracker.domain.datetime.CurrentLocalDateTimeProvider
import com.medicine.intake.tracker.domain.intake.IntakeMapper
import com.medicine.intake.tracker.domain.intake.IntakeProvider
import com.medicine.intake.tracker.domain.medicine.CurrentMedicineIdProvider
import com.medicine.intake.tracker.domain.settings.provider.YesterdayHoursProvider
import kotlinx.coroutines.flow.combine
import java.time.Duration

class RecentIntakesUseCase(
    currentLocalDateTimeProvider: CurrentLocalDateTimeProvider,
    intakeProvider: IntakeProvider,
    currentMedicineIdProvider: CurrentMedicineIdProvider,
    yesterdayHoursProvider: YesterdayHoursProvider
) {
    val recentIntakes = combine(
        currentLocalDateTimeProvider.localDateTimeAtStartOfDay,
        intakeProvider.sortedIntakes,
        currentMedicineIdProvider.currentMedicineId,
        yesterdayHoursProvider.yesterdayHours,
    ) { localDateTimeAtStartOfDay, sortedIntakes, currentMedicineId, yesterdayHours ->
        val intakeByCurrentMedicineId = sortedIntakes.filter { it.medicineId == currentMedicineId }
        intakeByCurrentMedicineId.filter { intake ->

            val intakeDateTime = IntakeMapper.stringToLocalDate(intake.date).atTime(
                IntakeMapper.stringToLocalTime(intake.time)
            )
            val intakeDate = intakeDateTime.toLocalDate()

            val dateDuration = Duration.between(intakeDate.atStartOfDay(), localDateTimeAtStartOfDay)

            if (dateDuration.toDays() == 0L) {
                true
            } else if (dateDuration.toDays() == 1L) {
                val durationFromStartOfDay = Duration.between(intakeDateTime, localDateTimeAtStartOfDay)
                durationFromStartOfDay.toHours() <= yesterdayHours
            } else false
        }
    }
}