package com.objectorientedoleg.feature.home

import com.objectorientedoleg.core.domain.model.MoviesItem
import kotlinx.collections.immutable.ImmutableList

internal sealed interface HomeUiState {

    data object Loading : HomeUiState

    data object NotLoaded : HomeUiState

    data class Loaded(val movies: ImmutableList<MoviesItem>) : HomeUiState
}