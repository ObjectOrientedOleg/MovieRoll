package com.objectorientedoleg.data.repository

import androidx.paging.PagingData
import com.objectorientedoleg.data.model.Movie
import com.objectorientedoleg.data.sync.Syncable
import kotlinx.coroutines.flow.Flow

enum class MovieType {
    NowPlaying,
    Popular,
    TopRated,
    UpComing
}

interface MoviesRepository : Syncable {

    fun getMovies(movieType: MovieType): Flow<PagingData<Movie>>
}