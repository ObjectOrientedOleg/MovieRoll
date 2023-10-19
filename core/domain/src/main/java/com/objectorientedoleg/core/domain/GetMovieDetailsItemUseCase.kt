package com.objectorientedoleg.core.domain

import com.objectorientedoleg.core.common.immutable.mapNotNullToImmutableList
import com.objectorientedoleg.core.common.immutable.mapToImmutableList
import com.objectorientedoleg.core.data.model.MovieDetails
import com.objectorientedoleg.core.data.repository.ImageParams
import com.objectorientedoleg.core.data.repository.ImageRepository
import com.objectorientedoleg.core.data.repository.MovieDetailsRepository
import com.objectorientedoleg.core.data.type.ImageType
import com.objectorientedoleg.core.domain.model.MovieDetailsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class GetMovieDetailsItemUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val imageRepository: ImageRepository
) {

    operator fun invoke(movieId: String): Flow<MovieDetailsItem?> =
        movieDetailsRepository.getMovieDetails(movieId)
            .map { result ->
                val movieDetails = result.getOrNull()
                movieDetails?.asModel(imageRepository)
            }
}

private suspend fun MovieDetails.asModel(imageRepository: ImageRepository) =
    MovieDetailsItem(
        backdropUrls = images.toBackdropUrls(imageRepository),
        budget = budget,
        certification = certification,
        credits = credits.asModel(imageRepository),
        genres = genres.mapToImmutableList(MovieDetails.Genre::asModel),
        id = id,
        overview = overview,
        popularity = popularity.toDecimalString(),
        posterUrl = posterPath?.toImageUrl(imageRepository, ImageType.Poster),
        releaseDate = releaseDate.toReleaseDateString(),
        revenue = revenue,
        runtime = runtime?.toRuntimeString(),
        status = status,
        tagline = tagline,
        title = title,
        voteAverage = voteAverage.toDecimalString(),
        voteCount = voteCount
    )

private suspend fun MovieDetails.Credits.asModel(imageRepository: ImageRepository) =
    MovieDetailsItem.Credits(
        cast = cast.mapToImmutableList { it.asModel(imageRepository) },
        crew = crew.mapToImmutableList { it.asModel(imageRepository) }
    )

private suspend fun MovieDetails.Credits.Cast.asModel(imageRepository: ImageRepository) =
    MovieDetailsItem.Credits.Credit(
        name = name,
        description = character,
        profileUrl = profilePath?.toImageUrl(imageRepository, ImageType.Profile)
    )

private suspend fun MovieDetails.Credits.Crew.asModel(imageRepository: ImageRepository) =
    MovieDetailsItem.Credits.Credit(
        name = name,
        description = job,
        profileUrl = profilePath?.toImageUrl(imageRepository, ImageType.Profile)
    )

private fun MovieDetails.Genre.asModel() =
    MovieDetailsItem.Genre(
        id = id,
        name = name
    )

private suspend fun MovieDetails.Images.toBackdropUrls(imageRepository: ImageRepository) =
    backdrops.mapNotNullToImmutableList { image ->
        val backdrop = imageRepository.getImage(ImageParams(image.path, ImageType.Backdrop))
        backdrop?.url
    }

private suspend fun String.toImageUrl(
    imageRepository: ImageRepository,
    imageType: ImageType
) = let { posterPath ->
    val poster = imageRepository.getImage(ImageParams(posterPath, imageType))
    poster?.url
}

private fun String.toReleaseDateString() = let { releaseDate ->
    val date = releaseDate.toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.US)
    formatter.format(date.toJavaLocalDate())
}

private fun Int.toRuntimeString() = let { runTime ->
    if (runTime > 0) {
        val hours = runTime / 60
        val minutes = runTime % 60
        if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    } else {
        null
    }
}

private fun Double.toDecimalString() = String.format("%.1f", this)