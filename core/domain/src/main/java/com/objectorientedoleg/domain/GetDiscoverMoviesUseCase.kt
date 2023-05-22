package com.objectorientedoleg.domain

import androidx.paging.map
import com.objectorientedoleg.data.repository.ImageParams
import com.objectorientedoleg.data.repository.ImageRepository
import com.objectorientedoleg.data.repository.MovieQuery
import com.objectorientedoleg.data.repository.MoviesRepository
import com.objectorientedoleg.data.type.ImageType
import com.objectorientedoleg.domain.model.DiscoverMovie
import com.objectorientedoleg.domain.model.DiscoverMovies
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiscoverMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val imageRepository: ImageRepository
) {

    operator fun invoke(movieQuery: MovieQuery): DiscoverMovies = DiscoverMovies {
        moviesRepository.getMovies(movieQuery).map { pagingData ->
            pagingData.map { movie ->
                val image = movie.posterPath?.let { path ->
                    imageRepository.getImage(ImageParams(path, ImageType.Poster))
                }
                DiscoverMovie(
                    id = movie.id,
                    posterUrl = image?.url,
                    title = movie.title,
                    voteAverage = movie.voteAverage
                )
            }
        }
    }
}