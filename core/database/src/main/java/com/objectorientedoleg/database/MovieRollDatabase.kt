package com.objectorientedoleg.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.objectorientedoleg.database.dao.ImageConfigurationDao
import com.objectorientedoleg.database.model.ImageConfigurationEntity
import com.objectorientedoleg.database.util.InstantConverter
import com.objectorientedoleg.database.util.StringListConverter

internal const val DatabaseName = "movie-roll-database"

@Database(
    entities = [ImageConfigurationEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    InstantConverter::class,
    StringListConverter::class
)
abstract class MovieRollDatabase : RoomDatabase() {

    abstract fun imageConfigurationDao(): ImageConfigurationDao
}