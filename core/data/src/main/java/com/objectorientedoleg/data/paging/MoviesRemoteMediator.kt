package com.objectorientedoleg.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.objectorientedoleg.database.model.MovieEntity
import com.objectorientedoleg.database.model.MoviePageEntity
import com.objectorientedoleg.network.model.NetworkMovies

private const val MoviesFirstPageIndex = 1
private const val MoviesLastPageIndex = 500

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val localMoviesDataSource: LocalMoviesDataSource,
    private val remoteMoviesDataSource: RemoteMoviesDataSource
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction = InitializeAction.SKIP_INITIAL_REFRESH

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
            state.closestItemToPosition(position)?.let { popularMovie ->
                localMoviesDataSource.getPage(popularMovie.pageId)
            }
        }
    }

    private suspend fun getPageForFirstItem(state: PagingState<Int, MovieEntity>): MoviePageEntity? {
        return state.firstItemOrNull()?.let { popularMovie ->
            localMoviesDataSource.getPage(popularMovie.pageId)
        }
    }

    private suspend fun getPageForLastItem(state: PagingState<Int, MovieEntity>): MoviePageEntity? {
        return state.lastItemOrNull()?.let { popularMovie ->
            localMoviesDataSource.getPage(popularMovie.pageId)
        }
    }
}

private val MoviePageEntity.isFirstPage get() = page == MoviesFirstPageIndex

private val MoviePageEntity.isLastPage get() = page == totalPages || page == MoviesLastPageIndex

private val NetworkMovies.isEnd get() = movies.isEmpty()