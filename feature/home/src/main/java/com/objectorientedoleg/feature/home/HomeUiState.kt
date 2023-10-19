package com.objectorientedoleg.feature.home

import com.objectorientedoleg.core.domain.model.GenreItem

data class HomeUiState(val isSyncing: Boolean, val discoverGenresUiState: DiscoverGenresUiState)

sealed interface DiscoverGenresUiState {

    object Loading : DiscoverGenresUiState

    object NotLoaded : DiscoverGenresUiState

    data class Loaded(val genres: List<GenreItem>) : DiscoverGenresUiState
}