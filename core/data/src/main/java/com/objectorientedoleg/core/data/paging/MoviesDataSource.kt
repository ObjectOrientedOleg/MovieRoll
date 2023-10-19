package com.objectorientedoleg.core.data.paging

import com.objectorientedoleg.core.database.model.MoviePageEntity
import com.objectorientedoleg.core.network.model.NetworkMovies
import kotlinx.datetime.Instant

interface LocalMoviesDataSource {

    suspend fun lastUpdated(): Instant?

    suspend fun onLoadComplete(networkMovies: NetworkMovies, isRefresh: Boolean)

    suspend fun getPage(pageId: String): MoviePageEntity?
}

interface RemoteMoviesDataSource {

    suspend fun loadMovies(page: Int): Result<NetworkMovies>
}