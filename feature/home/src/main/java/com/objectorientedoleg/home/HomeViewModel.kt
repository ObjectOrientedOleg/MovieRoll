package com.objectorientedoleg.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.objectorientedoleg.data.sync.SyncManager
import com.objectorientedoleg.data.type.MovieType
import com.objectorientedoleg.domain.GetDiscoverMoviesUseCase
import com.objectorientedoleg.domain.model.DiscoverMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    syncManager: SyncManager,
    getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase,
    configuration: HomeScreenConfiguration
) : ViewModel() {

    val isSyncing: StateFlow<Boolean> = syncManager.isSyncing.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val nowPlayingMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.NowPlaying, configuration)

    val popularMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.Popular, configuration)

    val topRatedMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.TopRated, configuration)

    val upcomingMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.UpComing, configuration)

    private operator fun GetDiscoverMoviesUseCase.invoke(
        movieType: MovieType,
        configuration: HomeScreenConfiguration
    ) = this(movieType, configuration.discoverPosterSize).cachedIn(viewModelScope)
}