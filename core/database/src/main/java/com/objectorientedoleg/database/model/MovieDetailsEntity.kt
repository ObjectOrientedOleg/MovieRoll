package com.objectorientedoleg.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "movie_details_table")
data class MovieDetailsEntity(
    @ColumnInfo(name = "adult")
    val adult: Boolean,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,
    @ColumnInfo(name = "budget")
    val budget: Int,
    @ColumnInfo(name = "certification")
    val certification: String?,
    @ColumnInfo(name = "credits")
    val credits: Credits,
    @ColumnInfo(name = "genres")
    val genres: List<Genre>,
    @ColumnInfo(name = "homepage")
    val homepage: String?,
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "images")
    val images: Images,
    @ColumnInfo(name = "imdb_id")
    val imdbId: String?,
    @ColumnInfo(name = "original_language")
    val originalLanguage: String,
    @ColumnInfo(name = "original_title")
    val originalTitle: String,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "popularity")
    val popularity: Double,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "revenue")
    val revenue: Int,
    @ColumnInfo(name = "runtime")
    val runtime: Int?,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "tagline")
    val tagline: String?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "video")
    val video: Boolean,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "vote_count")
    val voteCount: Int,
    @ColumnInfo(name = "creation_date")
    val creationDate: Instant = Clock.System.now()
) {
    @Serializable
    data class Credits(
        @SerialName("cast") val cast: List<Cast>,
        @SerialName("crew") val crew: List<Crew>
    ) {
        @Serializable
        data class Cast(
            @SerialName("adult") val adult: Boolean,
            @SerialName("cast_id") val castId: Int,
            @SerialName("character") val character: String,
            @SerialName("credit_id") val creditId: String,
            @SerialName("gender") val gender: Int?,
            @SerialName("id") val id: Int,
            @SerialName("known_for_department") val knownForDepartment: String,
            @SerialName("name") val name: String,
            @SerialName("order") val order: Int,
            @SerialName("original_name") val originalName: String,
            @SerialName("popularity") val popularity: Double,
            @SerialName("profile_path") val profilePath: String?
        )

        @Serializable
        data class Crew(
            @SerialName("adult") val adult: Boolean,
            @SerialName("credit_id") val creditId: String,
            @SerialName("department") val department: String,
            @SerialName("gender") val gender: Int?,
            @SerialName("id") val id: Int,
            @SerialName("job") val job: String,
            @SerialName("known_for_department") val knownForDepartment: String,
            @SerialName("name") val name: String,
            @SerialName("original_name") val originalName: String,
            @SerialName("popularity") val popularity: Double,
            @SerialName("profile_path") val profilePath: String?
        )
    }

    @Serializable
    data class Genre(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String
    )

    @Serializable
    data class Images(
        @SerialName("backdrops") val backdrops: List<Image>,
        @SerialName("logos") val logos: List<Image>,
        @SerialName("posters") val posters: List<Image>
    ) {
        @Serializable
        data class Image(
            @SerialName("aspect_ratio") val aspectRatio: Double,
            @SerialName("file_path") val path: String,
            @SerialName("height") val height: Int,
            @SerialName("iso_639_1") val languageCode: String?,
            @SerialName("vote_average") val voteAverage: Double,
            @SerialName("vote_count") val voteCount: Int,
            @SerialName("width") val width: Int
        )
    }
}
