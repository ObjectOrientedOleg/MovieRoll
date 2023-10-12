package com.objectorientedoleg.domain

import com.objectorientedoleg.data.model.MovieDetails
import com.objectorientedoleg.data.repository.ImageParams
import com.objectorientedoleg.data.repository.ImageRepository
import com.objectorientedoleg.data.repository.MovieDetailsRepository
import com.objectorientedoleg.data.type.ImageType
import com.objectorientedoleg.domain.model.ImageUrl
import com.objectorientedoleg.domain.model.MovieDetailsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMovieDetailsItemUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val imageRepository: ImageRepository
) {

    operator fun invoke(movieId: String): Flow<MovieDetailsItem?> =
        movieDetailsRepository.getMovieDetails(movieId)
            .map { result ->
                result.getOrNull()?.let { movieDetails ->
                    val image = movieDetails.backdropPath?.let { path ->
                        imageRepository.getImage(ImageParams(path, ImageType.Backdrop))
                    }
                    movieDetails.asModel(image?.url)
                }
            }
}

private fun MovieDetails.asModel(backdropUrl: ImageUrl?) =
    MovieDetailsItem(
        backdropUrl = backdropUrl,
        budget = budget,
        certification = certification,
        credits = credits.asModel(),
        genres = genres.map(MovieDetails.Genre::asModel),
        id = id,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

private fun MovieDetails.Credits.asModel() =
    MovieDetailsItem.Credits(
        cast = cast.map(MovieDetails.Credits.Cast::asModel),
        crew = crew.map(MovieDetails.Credits.Crew::asModel)
    )

private fun MovieDetails.Credits.Cast.asModel() =
    MovieDetailsItem.Credits.Cast(
        character = character,
        name = name,
        order = order,
        profilePath = profilePath
    )

private fun MovieDetails.Credits.Crew.asModel() =
    MovieDetailsItem.Credits.Crew(
        job = job,
        name = name,
        profilePath = profilePath
    )

private fun MovieDetails.Genre.asModel() = MovieDetailsItem.Genre(name)