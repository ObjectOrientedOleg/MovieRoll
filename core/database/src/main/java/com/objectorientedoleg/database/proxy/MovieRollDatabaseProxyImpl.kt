package com.objectorientedoleg.database.proxy

import androidx.room.withTransaction
import com.objectorientedoleg.database.MovieRollDatabase
import com.objectorientedoleg.database.dao.GenreDao
import com.objectorientedoleg.database.dao.ImageConfigurationDao
import com.objectorientedoleg.database.dao.MovieDao
import com.objectorientedoleg.database.dao.MovieDetailsDao
import com.objectorientedoleg.database.dao.MoviePageDao
import javax.inject.Inject

internal class MovieRollDatabaseProxyImpl @Inject constructor(
    private val database: MovieRollDatabase
) : MovieRollDatabaseProxy {

    override val genreDao: GenreDao get() = database.genreDao()

    override val imageConfigurationDao: ImageConfigurationDao get() = database.imageConfigurationDao()

    override val movieDao: MovieDao get() = database.movieDao()

    override val movieDetailsDao: MovieDetailsDao get() = database.movieDetailsDao()

    override val moviePageDao: MoviePageDao get() = database.moviePageDao()

    override suspend fun asTransaction(block: suspend () -> Unit) = database.withTransaction(block)
}