package com.objectorientedoleg.core.domain.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

typealias PagingMoviesData = Flow<PagingData<MovieItem>>