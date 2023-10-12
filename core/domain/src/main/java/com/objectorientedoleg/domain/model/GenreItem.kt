package com.objectorientedoleg.domain.model

sealed interface GenreItem {
    val id: String
    val name: String

    data class SingleGenre(
        override val id: String,
        override val name: String,
        val movies: MoviesItem
    ) : GenreItem

    data class CombinedGenres(
        override val id: String,
        override val name: String,
        val genres: List<SingleGenre>
    ) : GenreItem
}