package com.objectorientedoleg.network

import com.objectorientedoleg.network.model.NetworkImageConfiguration
import com.objectorientedoleg.network.model.NetworkMovieDetails
import com.objectorientedoleg.network.model.NetworkMovies

interface MovieRollNetworkDataSource {

    suspend fun getImageConfiguration(): Result<NetworkImageConfiguration>

    suspend fun getNowPlayingMovies(page: Int): Result<NetworkMovies>

    suspend fun getPopularMovies(page: Int): Result<NetworkMovies>

    suspend fun getTopRatedMovies(page: Int): Result<NetworkMovies>

    suspend fun getUpcomingMovies(page: Int): Result<NetworkMovies>

    suspend fun getMovieDetails(movieId: Int): Result<NetworkMovieDetails>
}