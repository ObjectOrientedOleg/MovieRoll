package com.objectorientedoleg.database.proxy

import com.objectorientedoleg.database.dao.GenreDao
import com.objectorientedoleg.database.dao.ImageConfigurationDao
import com.objectorientedoleg.database.dao.MovieDao
import com.objectorientedoleg.database.dao.MovieDetailsDao
import com.objectorientedoleg.database.dao.MoviePageDao

interface MovieRollDatabaseProxy {

    val genreDao: GenreDao

    val imageConfigurationDao: ImageConfigurationDao

    val movieDao: MovieDao

    val movieDetailsDao: MovieDetailsDao

    val moviePageDao: MoviePageDao

    suspend fun asTransaction(block: suspend () -> Unit)
}