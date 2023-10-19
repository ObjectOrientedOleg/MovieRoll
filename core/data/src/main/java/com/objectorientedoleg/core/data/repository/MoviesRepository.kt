package com.objectorientedoleg.core.data.repository

import androidx.paging.PagingData
import com.objectorientedoleg.core.data.model.Movie
import com.objectorientedoleg.core.data.type.MovieType
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(movieQuery: MovieQuery): Flow<PagingData<Movie>>
}

sealed interface MovieQuery {
    val name: String

    data class ByType(val type: MovieType, override val name: String = type.name) : MovieQuery

    data class ByGenreId(val id: String, override val name: String) : MovieQuery
}