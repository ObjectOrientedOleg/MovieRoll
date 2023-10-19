package com.objectorientedoleg.core.data.repository

import com.objectorientedoleg.core.data.model.Genre
import com.objectorientedoleg.core.data.model.asModel
import com.objectorientedoleg.core.data.sync.Synchronizer
import com.objectorientedoleg.core.database.dao.GenreDao
import com.objectorientedoleg.core.database.model.GenreEntity
import com.objectorientedoleg.core.network.MovieRollNetworkDataSource
import com.objectorientedoleg.core.network.model.NetworkGenre
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

