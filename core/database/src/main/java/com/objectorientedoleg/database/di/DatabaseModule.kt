package com.objectorientedoleg.database.di

import android.content.Context
import androidx.room.Room
import com.objectorientedoleg.database.DatabaseName
import com.objectorientedoleg.database.MovieRollDatabase
import com.objectorientedoleg.database.dao.ImageConfigurationDao
import com.objectorientedoleg.database.dao.MovieDao
import com.objectorientedoleg.database.dao.MovieDetailsDao
import com.objectorientedoleg.database.dao.MoviePageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
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