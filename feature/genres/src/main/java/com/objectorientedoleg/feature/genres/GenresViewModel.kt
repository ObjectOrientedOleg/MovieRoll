package com.objectorientedoleg.feature.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.objectorientedoleg.core.common.immutable.mapToImmutableList
import com.objectorientedoleg.core.data.model.Genre
import com.objectorientedoleg.core.data.repository.GenreRepository
import com.objectorientedoleg.core.data.repository.MovieQuery
import com.objectorientedoleg.core.domain.GetMovieItemsUseCase
import com.objectorientedoleg.core.domain.model.GenreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
internal class GenresViewModel @Inject constructor(
    genreRepository: GenreRepository,
    getMovieItems: GetMovieItemsUseCase
) : ViewModel() {

    val uiState: StateFlow<GenresUiState> = genreRepository.getGenres()
        .map { genres ->
            if (genres.isEmpty()) {
                return@map GenresUiState.NotLoaded
            }
            val sortedGenres = genres.sortedBy { genre -> genre.name }
            val genreItems = sortedGenres.mapToImmutableList { genre ->
                val movieQuery = genre.toMovieQuery()
                GenreItem(
                    id = genre.id,
                    name = genre.name
                ) {
                    getMovieItems(movieQuery).cachedIn(viewModelScope)
                }
            }
            GenresUiState.Loaded(genreItems)
        }
        .onStart { emit(GenresUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = GenresUiState.NotLoaded
        )

    private var tabState = 0
    private val tabScrollStates = mutableMapOf<String, ScrollState>()

    fun restoreTabState(): Int = tabState

    fun saveTabState(index: Int) {
        tabState = index
    }

    fun restoreScrollState(genreId: String): ScrollState? = tabScrollStates[genreId]

    fun saveScrollState(genreId: String, scrollState: ScrollState) {
        tabScrollStates[genreId] = scrollState
    }
}

private fun Genre.toMovieQuery() = MovieQuery.ByGenreId(id = id, name = name)