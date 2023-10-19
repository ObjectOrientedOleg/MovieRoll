package com.objectorientedoleg.core.data.model

import com.objectorientedoleg.core.database.model.MovieDetailsEntity

data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String?,
    val budget: Int,
    val certification: String?,
    val credits: Credits,
    val genres: List<Genre>,
    val homepage: String?,
    val id: String,
    val images: Images,
    val imdbId: String?,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int?,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
) {
    data class Credits(
        val cast: List<Cast>,
        val crew: List<Crew>
    ) {
        data class Cast(
            val adult: Boolean,
            val castId: Int,
            val character: String,
            val creditId: String,
            val gender: Int?,
            val id: String,
            val knownForDepartment: String,
            val name: String,
            val order: Int,
            val originalName: String,
            val popularity: Double,
            val profilePath: String?
        )

        data class Crew(
            val adult: Boolean,
            val creditId: String,
            val department: String,
            val gender: Int?,
            val id: String,
            val job: String,
            val knownForDepartment: String,
            val name: String,
            val originalName: String,
            val popularity: Double,
            val profilePath: String?
        )
    }

    data class Genre(
        val id: String,
        val name: String
    )

    data class Images(
        val backdrops: List<Image>,
        val logos: List<Image>,
        val posters: List<Image>
    ) {
        data class Image(
            val aspectRatio: Double,
            val path: String,
            val height: Int,
            val languageCode: String?,
            val voteAverage: Double,
            val voteCount: Int,
            val width: Int
        )
    }
}

fun MovieDetailsEntity.asModel(): MovieDetails =
    MovieDetails(
        adult = adult,
        backdropPath = backdropPath,
        budget = budget,
        certification = certification,
        credits = credits.asModel(),
        genres = genres.map(MovieDetailsEntity.Genre::asModel),
        homepage = homepage,
        id = id,
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

private fun MovieDetailsEntity.Credits.asModel() =
    MovieDetails.Credits(
        cast = cast.map(MovieDetailsEntity.Credits.Cast::asModel),
        crew = crew.map(MovieDetailsEntity.Credits.Crew::asModel)
    )

private fun MovieDetailsEntity.Credits.Cast.asModel() =
    MovieDetails.Credits.Cast(
        adult = adult,
        castId = castId,
        character = character,
        creditId = creditId,
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        order = order,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath
    )

private fun MovieDetailsEntity.Credits.Crew.asModel() =
    MovieDetails.Credits.Crew(
        adult = adult,
        creditId = creditId,
        department = department,
        gender = gender,
        id = id,
        job = job,
        knownForDepartment = knownForDepartment,
        name = name,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath
    )

private fun MovieDetailsEntity.Genre.asModel() =
    MovieDetails.Genre(
        id = id,
        name = name
    )

private fun MovieDetailsEntity.Images.asModel() =
    MovieDetails.Images(
        backdrops = backdrops.map(MovieDetailsEntity.Images.Image::asModel),
        logos = logos.map(MovieDetailsEntity.Images.Image::asModel),
        posters = posters.map(MovieDetailsEntity.Images.Image::asModel)
    )

private fun MovieDetailsEntity.Images.Image.asModel() =
    MovieDetails.Images.Image(
        aspectRatio = aspectRatio,
        path = path,
        height = height,
        languageCode = languageCode,
        voteAverage = voteAverage,
        voteCount = voteCount,
        width = width
    )