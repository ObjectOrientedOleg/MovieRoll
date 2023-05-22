package com.objectorientedoleg.domain

import androidx.paging.map
import com.objectorientedoleg.data.repository.ImageUrlRepository
import com.objectorientedoleg.data.repository.MovieQuery
import com.objectorientedoleg.data.repository.MoviesRepository
import com.objectorientedoleg.domain.model.DiscoverMovie
import com.objectorientedoleg.domain.model.DiscoverMovies
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiscoverMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val imageUrlRepository: ImageUrlRepository
) {

    operator fun invoke(movieQuery: MovieQuery): DiscoverMovies = DiscoverMovies {
        moviesRepository.getMovies(movieQuery).map { pagingData ->
            pagingData.map { movie ->
                DiscoverMovie(
                    id = movie.id,
                    posterUrl = null,
                    title = movie.title,
                    voteAverage = movie.voteAverage
                )
            }
        }
    }
}