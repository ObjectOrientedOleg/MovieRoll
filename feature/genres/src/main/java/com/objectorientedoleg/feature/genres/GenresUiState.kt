package com.objectorientedoleg.feature.genres

import com.objectorientedoleg.domain.model.GenreItem
import kotlinx.collections.immutable.ImmutableList

internal sealed interface GenresUiState {

    data object Loading : GenresUiState

    data object NotLoaded : GenresUiState

    data class Loaded(val genres: ImmutableList<GenreItem>) : GenresUiState
}

internal data class ScrollState(val index: Int, val offset: Int)