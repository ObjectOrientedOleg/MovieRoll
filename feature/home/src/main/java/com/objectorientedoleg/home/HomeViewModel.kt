package com.objectorientedoleg.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.objectorientedoleg.data.model.Genre
import com.objectorientedoleg.data.repository.GenreRepository
import com.objectorientedoleg.data.repository.MovieQuery
import com.objectorientedoleg.data.sync.SyncManager
import com.objectorientedoleg.data.type.MovieType
import com.objectorientedoleg.domain.GetMovieItemsUseCase
import com.objectorientedoleg.domain.model.GenreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    syncManager: SyncManager,
    genreRepository: GenreRepository,
    getMovieItems: GetMovieItemsUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        syncManager.isSyncing,
        genreRepository.getDiscoverGenres(getMovieItems)
    ) { isSyncing, discoverGenresUiState ->
        HomeUiState(isSyncing = isSyncing, discoverGenresUiState = discoverGenresUiState)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(
                isSyncing = false,
                discoverGenresUiState = DiscoverGenresUiState.NotLoaded
            )
        )
}

private fun GenreRepository.getDiscoverGenres(
    getMovieItems: GetMovieItemsUseCase
) = getGenres().map { genres ->
    if (genres.isEmpty()) {
        return@map DiscoverGenresUiState.NotLoaded
    }
    val genreItems = genres.mapTo(mutableListOf<GenreItem>()) { genre ->
        GenreItem.SingleGenre(
            id = genre.id,
            name = genre.name,
            init = { getMovieItems(genre.toMovieQuery()) }
        )
    }
    genreItems.apply {
        sortBy { discoverGenre -> discoverGenre.name }
        val singleGenres = MovieType.values().map { movieType ->
            GenreItem.SingleGenre(
                id = movieType.name,
                name = movieType.displayName(),
                init = { getMovieItems(movieType.toMovieQuery()) }
            )
        }
        add(
            0,
            GenreItem.CombinedGenres(
                id = "Recommended",
                name = "Recommended",
                genres = singleGenres
            )
        )
    }
    DiscoverGenresUiState.Loaded(genreItems)
}
    .onStart { emit(DiscoverGenresUiState.Loading) }

private fun Genre.toMovieQuery() = MovieQuery.ByGenreId(id = id, name = name)

private fun MovieType.toMovieQuery() = MovieQuery.ByType(this)

private fun MovieType.displayName() = when (this) {
    MovieType.NowPlaying -> "Now playing"
    MovieType.Popular -> "Popular"
    MovieType.TopRated -> "Top rated"
    MovieType.UpComing -> "Upcoming"
}