package com.objectorientedoleg.domain.model

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

@Immutable
class MoviesItem(initializer: () -> Flow<PagingData<MovieItem>>) {
    val paging: Flow<PagingData<MovieItem>> by lazy(initializer)
}
