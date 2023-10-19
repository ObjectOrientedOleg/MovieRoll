package com.objectorientedoleg.core.data.repository

import com.objectorientedoleg.core.data.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {

    fun getMovieDetails(movieId: String): Flow<Result<MovieDetails>>
}