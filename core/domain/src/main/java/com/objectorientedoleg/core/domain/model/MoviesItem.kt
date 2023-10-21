package com.objectorientedoleg.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class MoviesItem(val name: String, val movies: PagingMoviesData)