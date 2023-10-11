package com.objectorientedoleg.data.repository

import com.objectorientedoleg.data.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {

    fun getMovieDetails(movieId: String): Flow<Result<MovieDetails>>
}