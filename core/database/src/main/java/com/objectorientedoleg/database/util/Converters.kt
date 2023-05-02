package com.objectorientedoleg.database.util

import androidx.room.TypeConverter
import com.objectorientedoleg.database.model.MovieDetailsEntity
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CreditsConverter {

    @TypeConverter
    fun creditsToString(value: MovieDetailsEntity.Credits): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonStringToCredits(json: String): MovieDetailsEntity.Credits =
        Json.decodeFromString(json)
}

class GenreListConverter {

    @TypeConverter
    fun genreListToJsonString(value: List<MovieDetailsEntity.Genre>): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonStringToGenreList(json: String): List<MovieDetailsEntity.Genre> =
        Json.decodeFromString(json)
}

class ImagesConverter {

    @TypeConverter
    fun creditsToString(value: MovieDetailsEntity.Images): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonStringToCredits(json: String): MovieDetailsEntity.Images =
        Json.decodeFromString(json)
}

class InstantConverter {

    @TypeConverter
    fun instantToLong(value: Instant): Long =
        value.toEpochMilliseconds()

    @TypeConverter
    fun longToInstant(value: Long): Instant =
        Instant.fromEpochMilliseconds(value)
}

class IntListConverter {

    @TypeConverter
    fun intListToJsonString(value: List<Int>): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonStringToIntList(json: String): List<Int> =
        Json.decodeFromString(json)
}

class StringListConverter {

    @TypeConverter
    fun stringListToJsonString(value: List<String>): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonStringToStringList(json: String): List<String> =
        Json.decodeFromString(json)
}