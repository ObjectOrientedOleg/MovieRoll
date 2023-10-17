package com.objectorientedoleg.feature.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.objectorientedoleg.common.immutable.mapToImmutableList
import com.objectorientedoleg.data.model.Genre
import com.objectorientedoleg.data.repository.GenreRepository
import com.objectorientedoleg.data.repository.MovieQuery
import com.objectorientedoleg.data.sync.SyncManager
import com.objectorientedoleg.domain.GetMoviesItemUseCase
import com.objectorientedoleg.domain.model.GenreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
internal class GenresViewModel @Inject constructor(
    syncManager: SyncManager,
    genreRepository: GenreRepository,
    getMovieItems: GetMoviesItemUseCase
) : ViewModel() {

    val isSyncing: StateFlow<Boolean> = syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val uiState: StateFlow<GenresUiState> = genreRepository.getGenreItems(getMovieItems)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GenresUiState.NotLoaded
        )
}

private fun GenreRepository.getGenreItems(getMovieItems: GetMoviesItemUseCase) =
    getGenres().map { genres ->
        if (genres.isEmpty()) {
            return@map GenresUiState.NotLoaded
        }
        val sortedGenres = genres.sortedBy { genre -> genre.name }
        val genreItems = sortedGenres.mapToImmutableList { genre ->
            GenreItem.SingleGenre(
                id = genre.id,
                name = genre.name,
                movies = getMovieItems(genre.toMovieQuery())
            )
        }
        GenresUiState.Loaded(genreItems)
    }
        .onStart { emit(GenresUiState.Loading) }

private fun Genre.toMovieQuery() = MovieQuery.ByGenreId(id = id, name = name)