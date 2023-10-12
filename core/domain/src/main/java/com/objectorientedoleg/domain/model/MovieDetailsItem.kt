package com.objectorientedoleg.domain.model

data class MovieDetailsItem(
    val backdropUrl: ImageUrl?,
    val budget: Int,
    val certification: String?,
    val credits: Credits,
    val genres: List<Genre>,
    val id: String,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int?,
    val status: String,
    val tagline: String?,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
) {

    data class Credits(
        val cast: List<Cast>,
        val crew: List<Crew>
    ) {
        data class Cast(
            val character: String,
            val name: String,
            val order: Int,
            val profilePath: String?
        )

        data class Crew(
            val job: String,
            val name: String,
            val profilePath: String?
        )
    }

    data class Genre(val name: String)
}