package com.objectorientedoleg.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.objectorientedoleg.core.data.repository.ImageParams
import com.objectorientedoleg.core.data.repository.ImageRepository
import com.objectorientedoleg.core.data.repository.MovieQuery
import com.objectorientedoleg.core.data.repository.MoviesRepository
import com.objectorientedoleg.core.data.type.ImageType
import com.objectorientedoleg.core.domain.model.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toLocalDate
import javax.inject.Inject

class GetMovieItemsUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val imageRepository: ImageRepository
) {

    operator fun invoke(movieQuery: MovieQuery): Flow<PagingData<MovieItem>> =
        moviesRepository.getMovies(movieQuery).map { pagingData ->
            pagingData.map { movie ->
                val image = movie.posterPath?.let { path ->
                    imageRepository.getImage(ImageParams(path, ImageType.Poster))
                }
                val releaseYear = movie.releaseDate.let { date ->
                    val localDate = date.toLocalDate()
                    localDate.year
                }
                MovieItem(
                    id = movie.id,
                    posterUrl = image?.url,
                    releaseYear = releaseYear,
                    title = movie.title,
                    voteAverage = movie.voteAverage
                )
            }
        }
}