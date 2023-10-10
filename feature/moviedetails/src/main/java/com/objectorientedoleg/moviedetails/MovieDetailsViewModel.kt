package com.objectorientedoleg.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.objectorientedoleg.moviedetails.navigation.MovieIdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<MovieDetailsUiState> = MutableStateFlow(MovieDetailsUiState.Loading)
}

private fun SavedStateHandle.getMovieId() = checkNotNull(get<String>(MovieIdArg)) {
    "The argument $MovieIdArg must not be null."
}