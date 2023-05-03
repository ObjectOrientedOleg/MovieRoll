package com.objectorientedoleg.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.objectorientedoleg.colorpalette.ColorPaletteProducer
import com.objectorientedoleg.data.model.ImageType
import com.objectorientedoleg.data.model.ImageUrlParams
import com.objectorientedoleg.data.repository.ImageUrlRepository
import com.objectorientedoleg.data.repository.MoviesRepository
import com.objectorientedoleg.data.type.MovieType
import com.objectorientedoleg.domain.model.DiscoverMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiscoverMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val imageUrlRepository: ImageUrlRepository,
    private val colorPaletteProducer: ColorPaletteProducer
) {

    operator fun invoke(movieType: MovieType, posterSize: Int): Flow<PagingData<DiscoverMovie>> =
        moviesRepository.getMovies(movieType)
            .map { pagingData ->
                pagingData.map { movie ->
                    val imageUrlParams = movie.posterPath?.let {
                        ImageUrlParams(it, posterSize, ImageType.Poster)
                    }
                    val posterUrl = imageUrlParams?.let {
                        imageUrlRepository.urlFromParams(it)
                    }
                    val colorPalette = posterUrl?.let {
                        colorPaletteProducer.paletteFromImage(it).getOrNull()
                    }
                    DiscoverMovie(
                        colorPalette = colorPalette,
                        id = movie.id,
                        posterUrl = posterUrl,
                        releaseDate = movie.releaseDate,
                        title = movie.title,
                        voteAverage = movie.voteAverage
                    )
                }
            }
}