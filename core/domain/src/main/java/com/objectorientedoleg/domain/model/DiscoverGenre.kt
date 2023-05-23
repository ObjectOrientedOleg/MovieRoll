package com.objectorientedoleg.domain.model

sealed interface DiscoverGenre {
    val id: Int
    val name: String

    data class SingleGenre(
        override val id: Int,
        override val name: String,
        val movies: DiscoverMovies
    ) : DiscoverGenre

    data class CombinedGenres(
        override val id: Int,
        override val name: String,
        val genres: List<SingleGenre>
    ) : DiscoverGenre
}