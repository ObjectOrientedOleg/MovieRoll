package com.objectorientedoleg.core.database.di

import android.content.Context
import androidx.room.Room
import com.objectorientedoleg.core.database.DatabaseName
import com.objectorientedoleg.core.database.MovieRollDatabase
import com.objectorientedoleg.core.database.dao.GenreDao
import com.objectorientedoleg.core.database.dao.ImageConfigurationDao
import com.objectorientedoleg.core.database.dao.MovieDao
import com.objectorientedoleg.core.database.dao.MovieDetailsDao
import com.objectorientedoleg.core.database.dao.MoviePageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [InternalDatabaseModule::class])
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesMovieRollDatabase(
        @ApplicationContext context: Context
    ): MovieRollDatabase =
        Room.databaseBuilder(
            context,
            MovieRollDatabase::class.java,
            DatabaseName
        ).build()

    @Provides
    fun providesGenreDao(
        database: MovieRollDatabase
    ): GenreDao = database.genreDao()

    @Provides
    fun providesImageConfigurationDao(
        database: MovieRollDatabase
    ): ImageConfigurationDao = database.imageConfigurationDao()

    @Provides
    fun providesMovieDao(
        database: MovieRollDatabase
    ): MovieDao = database.movieDao()

    @Provides
    fun providesMovieDetailsDao(
        database: MovieRollDatabase
    ): MovieDetailsDao = database.movieDetailsDao()

    @Provides
    fun providesMoviePageDao(
        database: MovieRollDatabase
    ): MoviePageDao = database.moviePageDao()
}