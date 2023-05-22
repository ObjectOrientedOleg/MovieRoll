package com.objectorientedoleg.domain.model

data class DiscoverMovie(
    val id: Int,
    val posterUrl: String?,
    val title: String,
    val voteAverage: Double
)