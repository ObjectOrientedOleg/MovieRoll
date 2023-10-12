package com.objectorientedoleg.moviedetails

import com.objectorientedoleg.domain.model.MovieDetailsItem
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal sealed interface MovieDetailsUiState {

    object Loading : MovieDetailsUiState

    object NotLoaded : MovieDetailsUiState

    data class Loaded(val item: MovieDetailsItem) : MovieDetailsUiState
}

@OptIn(ExperimentalContracts::class)
internal fun MovieDetailsUiState.isLoaded(): Boolean {
    contract {
        returns(true) implies (this@isLoaded is MovieDetailsUiState.Loaded)
    }
    return this is MovieDetailsUiState.Loaded
}
