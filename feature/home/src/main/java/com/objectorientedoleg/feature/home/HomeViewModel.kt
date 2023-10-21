package com.objectorientedoleg.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.objectorientedoleg.core.common.immutable.mapToImmutableList
import com.objectorientedoleg.core.data.repository.MovieQuery
import com.objectorientedoleg.core.data.type.MovieType
import com.objectorientedoleg.core.domain.GetMovieItemsUseCase
import com.objectorientedoleg.core.domain.model.MoviesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    getMovieItems: GetMovieItemsUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = flow<HomeUiState> {
        val movieTypes = MovieType.values().asList()
        val movies = movieTypes.mapToImmutableList { movieType ->
            val movieQuery = movieType.toMovieQuery()
            MoviesItem(
                name = movieType.displayName(),
                movies = getMovieItems(movieQuery).cachedIn(viewModelScope)
            )
        }
        emit(HomeUiState.Loaded(movies))
    }
        .onStart { emit(HomeUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = HomeUiState.NotLoaded
        )
}

private fun MovieType.toMovieQuery() = MovieQuery.ByType(this)

private fun MovieType.displayName() = when (this) {
    MovieType.NowPlaying -> "Now playing"
    MovieType.Popular -> "Popular"
    MovieType.TopRated -> "Top rated"
    MovieType.UpComing -> "Upcoming"
}