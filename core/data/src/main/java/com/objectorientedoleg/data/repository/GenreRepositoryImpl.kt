package com.objectorientedoleg.data.repository

import com.objectorientedoleg.data.model.Genre
import com.objectorientedoleg.data.model.asModel
import com.objectorientedoleg.data.sync.Synchronizer
import com.objectorientedoleg.database.dao.GenreDao
import com.objectorientedoleg.database.model.GenreEntity
import com.objectorientedoleg.network.MovieRollNetworkDataSource
import com.objectorientedoleg.network.model.NetworkGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GenreRepositoryImpl @Inject constructor(
    private val genreDao: GenreDao,
    private val networkDataSource: MovieRollNetworkDataSource,
) : GenreRepository {

    override fun getGenres(): Flow<List<Genre>> =
        genreDao.getAll().map(List<GenreEntity>::asModels)

    override suspend fun sync(synchronizer: Synchronizer): Boolean =
        synchronizer.sync(
            syncDate = { genreDao.lastUpdated() },
            onFetchUpdate = { networkDataSource.getGenres() },
            onRunUpdate = { networkGenres ->
                genreDao.deleteAndInsertAll(networkGenres.genres.asEntities())
                // Update was successful.
                true
            }
        )
}

private fun List<GenreEntity>.asModels() = map(GenreEntity::asModel)

private fun List<NetworkGenre>.asEntities() = map(NetworkGenre::asEntity)

private fun NetworkGenre.asEntity() = GenreEntity(id = id.toString(), name = name)

