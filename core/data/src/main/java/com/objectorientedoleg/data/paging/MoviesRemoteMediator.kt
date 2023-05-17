package com.objectorientedoleg.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.objectorientedoleg.data.sync.Synchronizer
import com.objectorientedoleg.database.model.MovieEntity
import com.objectorientedoleg.database.model.MoviePageEntity
import com.objectorientedoleg.network.model.NetworkMovies

internal const val MoviesPageSize = 40
internal const val MoviesFirstPageIndex = 1
internal const val MoviesLastPageIndex = 500

@OptIn(ExperimentalPagingApi::class)
internal class MoviesRemoteMediator(
    private val localMoviesDataSource: LocalMoviesDataSource,
    private val remoteMoviesDataSource: RemoteMoviesDataSource,
    private val synchronizer: Synchronizer
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return if (synchronizer.isDataValid(localMoviesDataSource.lastUpdated())) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val page = getPageClosestToCurrentPosition(state)
                page?.page ?: MoviesFirstPageIndex
            }

            LoadType.PREPEND -> {
                val page = getPageForFirstItem(state)
                if (page == null || page.isFirstPage) {
                    return MediatorResult.Success(endOfPaginationReached = page != null)
                }
                page.page.minus(1)
            }

            LoadType.APPEND -> {
                val page = getPageForLastItem(state)
                if (page == null || page.isLastPage) {
                    return MediatorResult.Success(endOfPaginationReached = page != null)
                }
                page.page.plus(1)
            }
        }

        return remoteMoviesDataSource.loadMovies(page).fold(
            onSuccess = { remoteMovies ->
                val endOfPaginationReached = remoteMovies.isEnd

                if (!endOfPaginationReached) {
                    val isRefresh = loadType == LoadType.REFRESH
                    localMoviesDataSource.onLoadComplete(remoteMovies, isRefresh)
                }

                MediatorResult.Success(endOfPaginationReached)
            },
            onFailure = { error ->
                MediatorResult.Error(error)
            }
        )
    }

    private suspend fun getPageClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): MoviePageEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { movieEntity ->
                localMoviesDataSource.getPage(movieEntity.pageId)
            }
        }
    }

    private suspend fun getPageForFirstItem(state: PagingState<Int, MovieEntity>): MoviePageEntity? {
        return state.firstItemOrNull()?.let { movieEntity ->
            localMoviesDataSource.getPage(movieEntity.pageId)
        }
    }

    private suspend fun getPageForLastItem(state: PagingState<Int, MovieEntity>): MoviePageEntity? {
        return state.lastItemOrNull()?.let { movieEntity ->
            localMoviesDataSource.getPage(movieEntity.pageId)
        }
    }
}

private val MoviePageEntity.isFirstPage get() = page == MoviesFirstPageIndex

private val MoviePageEntity.isLastPage get() = page == totalPages || page == MoviesLastPageIndex

private val NetworkMovies.isEnd get() = movies.isEmpty()