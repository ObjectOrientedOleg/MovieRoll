package com.objectorientedoleg.feature.genres

import com.objectorientedoleg.domain.model.GenreItem
import kotlinx.collections.immutable.ImmutableList

sealed interface GenresUiState {

    data object Loading : GenresUiState

    data object NotLoaded : GenresUiState

    data class Loaded(val genres: ImmutableList<GenreItem>) : GenresUiState
}