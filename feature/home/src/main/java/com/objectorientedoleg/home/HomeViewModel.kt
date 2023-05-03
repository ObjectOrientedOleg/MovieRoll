package com.objectorientedoleg.home

import androidx.lifecycle.SavedStateHandle
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isSyncing: StateFlow<Boolean> = syncManager.isSyncing.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val nowPlayingMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.NowPlaying)

    val popularMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.Popular)

    val topRatedMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.TopRated)

    val upcomingMovies: Flow<PagingData<DiscoverMovie>> =
        getDiscoverMoviesUseCase(MovieType.UpComing)

    private operator fun GetDiscoverMoviesUseCase.invoke(
        movieType: MovieType
    ): Flow<PagingData<DiscoverMovie>> {
        val configuration = checkNotNull(
            savedStateHandle.get<HomeScreenConfiguration>(ConfigurationArg)
        )
        return this(movieType, configuration.discoverPosterSize)
            .cachedIn(viewModelScope)
    }
}