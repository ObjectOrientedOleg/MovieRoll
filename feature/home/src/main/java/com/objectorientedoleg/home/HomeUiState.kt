package com.objectorientedoleg.home

import com.objectorientedoleg.domain.model.DiscoverGenre

data class HomeUiState(val isSyncing: Boolean, val discoverGenresUiState: DiscoverGenresUiState)

sealed interface DiscoverGenresUiState {

    object Loading : DiscoverGenresUiState

    object NotLoaded : DiscoverGenresUiState

    data class Loaded(val genres: List<DiscoverGenre>) : DiscoverGenresUiState
}