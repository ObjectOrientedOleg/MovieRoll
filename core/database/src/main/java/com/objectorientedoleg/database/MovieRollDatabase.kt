package com.objectorientedoleg.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.objectorientedoleg.database.dao.ImageConfigurationDao
import com.objectorientedoleg.database.dao.MovieDao
import com.objectorientedoleg.database.dao.MovieDetailsDao
import com.objectorientedoleg.database.dao.MoviePageDao
import com.objectorientedoleg.database.model.ImageConfigurationEntity
import com.objectorientedoleg.database.model.MovieDetailsEntity
import com.objectorientedoleg.database.model.MovieEntity
import com.objectorientedoleg.database.model.MoviePageEntity
import com.objectorientedoleg.database.util.CreditsConverter
import com.objectorientedoleg.database.util.GenreListConverter
import com.objectorientedoleg.database.util.ImagesConverter
import com.objectorientedoleg.database.util.InstantConverter
import com.objectorientedoleg.database.util.IntListConverter
import com.objectorientedoleg.database.util.StringListConverter

internal const val DatabaseName = "movie_roll_database"

@Database(
    entities = [
        ImageConfigurationEntity::class,
        MovieDetailsEntity::class,
        MovieEntity::class,
        MoviePageEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    CreditsConverter::class,
    GenreListConverter::class,
    ImagesConverter::class,
    InstantConverter::class,
    IntListConverter::class,
    StringListConverter::class
)
abstract class MovieRollDatabase : RoomDatabase() {

    abstract fun imageConfigurationDao(): ImageConfigurationDao

    abstract fun movieDao(): MovieDao

    abstract fun movieDetailsDao(): MovieDetailsDao

    abstract fun moviePageDao(): MoviePageDao
}