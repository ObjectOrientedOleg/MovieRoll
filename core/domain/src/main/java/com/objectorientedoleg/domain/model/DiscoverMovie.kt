package com.objectorientedoleg.domain.model

data class DiscoverMovie(
    val id: Int,
    val posterUrl: ImageUrl?,
    val releaseYear: Int,
    val title: String,
    val voteAverage: Double
)