package com.objectorientedoleg.moviedetails

import com.objectorientedoleg.domain.model.MovieDetailsItem

internal sealed interface MovieDetailsUiState {

    data object Loading : MovieDetailsUiState

    data object NotLoaded : MovieDetailsUiState

    data class Loaded(val movieDetails: MovieDetailsItem) : MovieDetailsUiState
}

internal val MovieDetailsUiState.title: String
    get() = (this as? MovieDetailsUiState.Loaded)?.movieDetails?.title.orEmpty()
