package com.medicine.intake.tracker.data.intake.conversion

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTimeTypeConverter {
    @TypeConverter
    fun fromEpochSeconds(value: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC)
    }

    @TypeConverter
    fun toEpochSeconds(value: LocalDateTime): Long {
        return value.toEpochSecond(ZoneOffset.UTC)
    }
}