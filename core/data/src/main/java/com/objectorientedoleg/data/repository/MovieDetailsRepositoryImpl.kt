package com.objectorientedoleg.data.repository

import com.objectorientedoleg.data.model.MovieDetails
import com.objectorientedoleg.data.model.asModel
import com.objectorientedoleg.data.sync.Synchronizer
import com.objectorientedoleg.database.dao.MovieDetailsDao
import com.objectorientedoleg.database.model.MovieDetailsEntity
import com.objectorientedoleg.network.MovieRollNetworkDataSource
import com.objectorientedoleg.network.model.NetworkGenre
import com.objectorientedoleg.network.model.NetworkMovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class MovieDetailsRepositoryImpl @Inject constructor(
    private val movieDetailsDao: MovieDetailsDao,
    private val networkDataSource: MovieRollNetworkDataSource,
    private val synchronizer: Synchronizer
) : MovieDetailsRepository {

    override fun getMovieDetails(movieId: String): Flow<Result<MovieDetails>> = flow {
        val cachedEntity = movieDetailsDao.getMovieDetails(movieId)
        if (cachedEntity != null && synchronizer.isDataValid(cachedEntity.creationDate)) {
            emit(Result.success(cachedEntity.asModel()))
        } else {
            networkDataSource.getMovieDetails(movieId.toInt()).fold(
                onSuccess = { networkMovieDetails ->
                    val newEntity = networkMovieDetails.asEntity()
                    movieDetailsDao.insert(newEntity)
                    emit(Result.success(newEntity.asModel()))
                },
                onFailure = { exception ->
                    emit(Result.failure(exception))
                }
            )
        }
    }
}

private fun NetworkMovieDetails.asEntity() =
    MovieDetailsEntity(
        adult = adult,
        backdropPath = backdropPath,
        budget = budget,
        certification = releaseDates.findCertification(),
        credits = credits.asModel(),
        genres = genres.map(NetworkGenre::asModel),
        homepage = homepage,
        id = id.toString(),
        images = images.asModel(),
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

private fun NetworkMovieDetails.ReleaseDates.findCertification(): String? {
    val countryReleaseDates = countryReleaseDates.find { country ->
        country.countryCode == "US"
    }
    val releaseDate = countryReleaseDates?.releaseDates?.find { releaseDate ->
        releaseDate.certification.isNotEmpty()
    }
    return releaseDate?.certification
}

private fun NetworkMovieDetails.Credits.asModel() =
    MovieDetailsEntity.Credits(
        cast = cast.map(NetworkMovieDetails.Credits.Cast::asModel),
        crew = crew.map(NetworkMovieDetails.Credits.Crew::asModel)
    )

private fun NetworkMovieDetails.Credits.Cast.asModel() =
    MovieDetailsEntity.Credits.Cast(
        adult = adult,
        castId = castId,
        character = character,
        creditId = creditId,
        gender = gender,
        id = id.toString(),
        knownForDepartment = knownForDepartment,
        name = name,
        order = order,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath
    )

private fun NetworkMovieDetails.Credits.Crew.asModel() =
    MovieDetailsEntity.Credits.Crew(
        adult = adult,
        creditId = creditId,
        department = department,
        gender = gender,
        id = id.toString(),
        job = job,
        knownForDepartment = knownForDepartment,
        name = name,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath
    )

private fun NetworkGenre.asModel() =
    MovieDetailsEntity.Genre(
        id = id.toString(),
        name = name
    )

private fun NetworkMovieDetails.Images.asModel() =
    MovieDetailsEntity.Images(
        backdrops = backdrops.map(NetworkMovieDetails.Images.Image::asModel),
        logos = logos.map(NetworkMovieDetails.Images.Image::asModel),
        posters = posters.map(NetworkMovieDetails.Images.Image::asModel)
    )

private fun NetworkMovieDetails.Images.Image.asModel() =
    MovieDetailsEntity.Images.Image(
        aspectRatio = aspectRatio,
        path = path,
        height = height,
        languageCode = languageCode,
        voteAverage = voteAverage,
        voteCount = voteCount,
        width = width
    )