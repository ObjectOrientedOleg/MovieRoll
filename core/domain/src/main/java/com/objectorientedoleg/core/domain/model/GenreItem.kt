package com.objectorientedoleg.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
class GenreItem(
    val id: String,
    val name: String,
    initMovies: () -> PagingMoviesData
) {
    val movies: PagingMoviesData by lazy(initMovies)

    override fun toString(): String = "GenreItem(id=$id, name=$name)"
}