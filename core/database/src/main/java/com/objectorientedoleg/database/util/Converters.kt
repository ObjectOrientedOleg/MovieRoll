package com.objectorientedoleg.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class InstantConverter {

    @TypeConverter
    fun instantToLong(value: Instant): Long =
        value.toEpochMilliseconds()

    @TypeConverter
    fun longToInstant(value: Long): Instant =
        Instant.fromEpochMilliseconds(value)
}

class StringListConverter {

    @TypeConverter
    fun stringListToJsonString(value: List<String>): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonStringToStringList(json: String): List<String> =
        Json.decodeFromString(json)
}