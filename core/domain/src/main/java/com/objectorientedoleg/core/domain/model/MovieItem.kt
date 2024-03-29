package com.objectorientedoleg.core.domain.model

data class MovieItem(
    val id: String,
    val posterUrl: ImageUrl?,
    val releaseYear: Int,
    val title: String,
    val voteAverage: Int
)