package com.objectorientedoleg.data.repository

import androidx.paging.PagingData
import com.objectorientedoleg.data.model.Movie
import com.objectorientedoleg.data.sync.Syncable
import com.objectorientedoleg.data.type.MovieType
import kotlinx.coroutines.flow.Flow

interface MoviesRepository : Syncable {

    fun getMovies(movieType: MovieType): Flow<PagingData<Movie>>
}