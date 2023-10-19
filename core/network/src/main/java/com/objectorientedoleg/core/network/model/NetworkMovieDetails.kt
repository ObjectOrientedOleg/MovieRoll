package com.objectorientedoleg.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMovieDetails(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("budget") val budget: Int,
    @SerialName("credits") val credits: Credits,
    @SerialName("genres") val genres: List<NetworkGenre>,
    @SerialName("homepage") val homepage: String?,
    @SerialName("id") val id: Int,
    @SerialName("images") val images: Images,
    @SerialName("imdb_id") val imdbId: String?,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("overview") val overview: String?,
    @SerialName("popularity") val popularity: Double,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_dates") val releaseDates: ReleaseDates,
    @SerialName("revenue") val revenue: Int,
    @SerialName("runtime") val runtime: Int?,
    @SerialName("status") val status: String,
    @SerialName("tagline") val tagline: String?,
    @SerialName("title") val title: String,
    @SerialName("video") val video: Boolean,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
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

    @Serializable
    data class ReleaseDates(
        @SerialName("results") val countryReleaseDates: List<CountryReleaseDates>
    ) {
        @Serializable
        data class CountryReleaseDates(
            @SerialName("iso_3166_1") val countryCode: String,
            @SerialName("release_dates") val releaseDates: List<ReleaseDate>
        ) {
            @Serializable
            data class ReleaseDate(
                @SerialName("certification") val certification: String,
                @SerialName("iso_639_1") val languageCode: String,
                @SerialName("note") val note: String,
                @SerialName("release_date") val releaseDate: String,
                @SerialName("type") val type: Int
            )
        }
    }
}