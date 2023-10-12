package com.objectorientedoleg.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.objectorientedoleg.domain.GetMovieDetailsItemUseCase
import com.objectorientedoleg.moviedetails.navigation.MovieIdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class MovieDetailsViewModel @Inject constructor(
    getMovieDetailsItem: GetMovieDetailsItemUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<MovieDetailsUiState> =
        getMovieDetailsItem(savedStateHandle.getMovieId())
            .map { item ->
                item?.let(MovieDetailsUiState::Loaded)
                    ?: MovieDetailsUiState.NotLoaded
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MovieDetailsUiState.Loading
            )
}

private fun SavedStateHandle.getMovieId() = checkNotNull(get<String>(MovieIdArg)) {
    "The argument $MovieIdArg must not be null."
}