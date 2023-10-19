package com.objectorientedoleg.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.objectorientedoleg.core.database.dao.GenreDao
import com.objectorientedoleg.core.database.dao.ImageConfigurationDao
import com.objectorientedoleg.core.database.dao.MovieDao
import com.objectorientedoleg.core.database.dao.MovieDetailsDao
import com.objectorientedoleg.core.database.dao.MoviePageDao
import com.objectorientedoleg.core.database.model.GenreEntity
import com.objectorientedoleg.core.database.model.ImageConfigurationEntity
import com.objectorientedoleg.core.database.model.MovieDetailsEntity
import com.objectorientedoleg.core.database.model.MovieEntity
import com.objectorientedoleg.core.database.model.MoviePageEntity
import com.objectorientedoleg.core.database.util.CreditsConverter
import com.objectorientedoleg.core.database.util.GenreListConverter
import com.objectorientedoleg.core.database.util.ImagesConverter
import com.objectorientedoleg.core.database.util.InstantConverter
import com.objectorientedoleg.core.database.util.IntListConverter
import com.objectorientedoleg.core.database.util.StringListConverter

internal const val DatabaseName = "movie_roll_database"

@Database(
    entities = [
        GenreEntity::class,
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

    abstract fun genreDao(): GenreDao

    abstract fun imageConfigurationDao(): ImageConfigurationDao

    abstract fun movieDao(): MovieDao

    abstract fun movieDetailsDao(): MovieDetailsDao

    abstract fun moviePageDao(): MoviePageDao
}