package com.objectorientedoleg.moviedetails

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal sealed interface MovieDetailsUiState {

    object Loading : MovieDetailsUiState

    object NotLoaded : MovieDetailsUiState

    data class Loaded(val title: String) : MovieDetailsUiState
}

@OptIn(ExperimentalContracts::class)
internal fun MovieDetailsUiState.isLoaded(): Boolean {
    contract {
        returns(true) implies (this@isLoaded is MovieDetailsUiState.Loaded)
    }
    return this is MovieDetailsUiState.Loaded
}
