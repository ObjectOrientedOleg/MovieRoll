package com.objectorientedoleg.core.database.proxy

import com.objectorientedoleg.core.database.dao.GenreDao
import com.objectorientedoleg.core.database.dao.ImageConfigurationDao
import com.objectorientedoleg.core.database.dao.MovieDao
import com.objectorientedoleg.core.database.dao.MovieDetailsDao
import com.objectorientedoleg.core.database.dao.MoviePageDao

interface MovieRollDatabaseProxy {

    val genreDao: GenreDao

    val imageConfigurationDao: ImageConfigurationDao

    val movieDao: MovieDao

    val movieDetailsDao: MovieDetailsDao

    val moviePageDao: MoviePageDao

    suspend fun asTransaction(block: suspend () -> Unit)
}