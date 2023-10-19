package com.objectorientedoleg.core.domain.model

import kotlinx.collections.immutable.ImmutableList

data class MovieDetailsItem(
    val backdropUrls: ImmutableList<ImageUrl>,
    val budget: Int,
    val certification: String?,
    val credits: Credits,
    val genres: ImmutableList<Genre>,
    val id: String,
    val overview: String?,
    val popularity: String,
    val posterUrl: ImageUrl?,
    val releaseDate: String,
    val revenue: Int,
    val runtime: String?,
    val status: String,
    val tagline: String?,
    val title: String,
    val voteAverage: String,
    val voteCount: Int,
) {

    data class Credits(
        val cast: ImmutableList<Credit>,
        val crew: ImmutableList<Credit>
    ) {
        data class Credit(
            val name: String,
            val description: String,
            val profileUrl: ImageUrl?
        )
    }

    data class Genre(val id: String, val name: String)
}