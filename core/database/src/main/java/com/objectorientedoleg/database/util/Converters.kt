package com.objectorientedoleg.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {

    @TypeConverter
    fun instantToLong(value: Instant): Long {
        return value.toEpochMilliseconds()
    }

    @TypeConverter
    fun longToInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }
}