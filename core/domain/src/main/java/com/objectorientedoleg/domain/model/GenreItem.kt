package com.objectorientedoleg.domain.model

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface GenreItem {
    val id: String
    val name: String
    val movies: Flow<PagingData<MovieItem>>

    @Immutable
    class SingleGenre(
        override val id: String,
        override val name: String,
        init: () -> Flow<PagingData<MovieItem>>
    ) : GenreItem {
        override val movies: Flow<PagingData<MovieItem>> by lazy(init)
    }

    data class CombinedGenres(
        override val id: String,
        override val name: String,
        val genres: List<SingleGenre>,
        override val movies: Flow<PagingData<MovieItem>> = flow { }
    ) : GenreItem
}