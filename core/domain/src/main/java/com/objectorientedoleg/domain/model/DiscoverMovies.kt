package com.objectorientedoleg.domain.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class DiscoverMovies(initializer: () -> Flow<PagingData<DiscoverMovie>>) {
    val paging: Flow<PagingData<DiscoverMovie>> by lazy(initializer)
}
