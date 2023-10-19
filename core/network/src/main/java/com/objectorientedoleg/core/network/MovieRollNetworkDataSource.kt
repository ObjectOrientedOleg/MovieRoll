package com.objectorientedoleg.core.network

import com.objectorientedoleg.core.network.model.NetworkGenres
import com.objectorientedoleg.core.network.model.NetworkImageConfiguration
import com.objectorientedoleg.core.network.model.NetworkMovieDetails
import com.objectorientedoleg.core.network.model.NetworkMovies

interface MovieRollNetworkDataSource {

    suspend fun getImageConfiguration(): Result<NetworkImageConfiguration>

    suspend fun getGenres(): Result<NetworkGenres>

    suspend fun getMoviesByGenre(genreId: Int, page: Int): Result<NetworkMovies>

    suspend fun getNowPlayingMovies(page: Int): Result<NetworkMovies>

    suspend fun getPopularMovies(page: Int): Result<NetworkMovies>

    suspend fun getTopRatedMovies(page: Int): Result<NetworkMovies>

    suspend fun getUpcomingMovies(page: Int): Result<NetworkMovies>

    suspend fun getMovieDetails(movieId: Int): Result<NetworkMovieDetails>
}