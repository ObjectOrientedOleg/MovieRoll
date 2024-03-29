package com.objectorientedoleg.feature.genres

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.objectorientedoleg.core.domain.model.GenreItem
import com.objectorientedoleg.core.domain.model.MovieItem
import com.objectorientedoleg.core.ui.components.*
import com.objectorientedoleg.core.ui.theme.ThemeDefaults
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GenresRoute(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenresViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GenresScreen(
        modifier = modifier,
        uiState = uiState,
        onSearchClick = onSearchClick,
        onSettingsClick = onSettingsClick,
        onMovieClick = onMovieClick,
        onRestoreTabState = viewModel::restoreTabState,
        onSaveTabState = viewModel::saveTabState,
        onRestoreScrollState = viewModel::restoreScrollState,
        onSaveScrollState = viewModel::saveScrollState,
    )
}

@Composable
private fun GenresScreen(
    uiState: GenresUiState,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onRestoreTabState: () -> Int,
    onSaveTabState: (Int) -> Unit,
    onRestoreScrollState: (String) -> ScrollState?,
    onSaveScrollState: (String, ScrollState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        MovieRollTopBar(
            title = stringResource(R.string.genres),
            onSearchClick = onSearchClick,
            onSettingsClick = onSettingsClick
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is GenresUiState.Loaded -> GenresLoadedLayout(
                    genreItems = uiState.genres,
                    onMovieClick = onMovieClick,
                    onRestoreTabState = onRestoreTabState,
                    onSaveTabState = onSaveTabState,
                    onRestoreScrollState = onRestoreScrollState,
                    onSaveScrollState = onSaveScrollState
                )

                is GenresUiState.Loading -> MovieRollLoadingIndicator(Modifier.align(Alignment.Center))
                is GenresUiState.NotLoaded -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GenresLoadedLayout(
    genreItems: ImmutableList<GenreItem>,
    onMovieClick: (String) -> Unit,
    onRestoreTabState: () -> Int,
    onSaveTabState: (Int) -> Unit,
    onRestoreScrollState: (String) -> ScrollState?,
    onSaveScrollState: (String, ScrollState) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialPage = remember { onRestoreTabState() }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = genreItems::size
    )
    DisposableEffect(Unit) {
        onDispose {
            onSaveTabState(pagerState.currentPage)
        }
    }

    TabLayout(
        modifier = modifier.fillMaxSize(),
        pagerState = pagerState,
        tabCount = genreItems.size,
        tabTitle = { index -> genreItems[index].name },
        tabKey = { index -> genreItems[index].id }
    ) { index ->
        GenreTab(
            genreItem = genreItems[index],
            onMovieClick = onMovieClick,
            onRestoreScrollState = onRestoreScrollState,
            onSaveScrollState = onSaveScrollState
        )
    }
}

@Composable
private fun GenreTab(
    genreItem: GenreItem,
    onMovieClick: (String) -> Unit,
    onRestoreScrollState: (String) -> ScrollState?,
    onSaveScrollState: (String, ScrollState) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = remember(genreItem.id) {
        onRestoreScrollState(genreItem.id)
    }
    val state = rememberLazyGridState(
        initialFirstVisibleItemIndex = scrollState?.index ?: 0,
        initialFirstVisibleItemScrollOffset = scrollState?.offset ?: 0
    )
    DisposableEffect(Unit) {
        onDispose {
            onSaveScrollState(
                genreItem.id,
                ScrollState(
                    index = state.firstVisibleItemIndex,
                    offset = state.firstVisibleItemScrollOffset
                )
            )
        }
    }

    val items = genreItem.movies.collectAsLazyPagingItems()
    if (items.isLoading) {
        GenreShimmerList(modifier)
    } else {
        GenreMovieList(
            modifier = modifier,
            state = state,
            movieItems = items,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun GenreShimmerList(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(ThemeDefaults.screenEdgePadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        mediumShimmerGrid(Modifier.fillMaxWidth())
    }
}

@Composable
private fun GenreMovieList(
    state: LazyGridState,
    movieItems: LazyPagingItems<MovieItem>,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = state,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(ThemeDefaults.screenEdgePadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        mediumMovieGrid(
            itemModifier = Modifier.fillMaxWidth(),
            movieItems = movieItems,
            onItemClick = { onMovieClick(it.id) }
        )
    }
}

private val LazyPagingItems<*>.isLoading
    get() = itemCount == 0 && loadState.refresh is LoadState.Loading
