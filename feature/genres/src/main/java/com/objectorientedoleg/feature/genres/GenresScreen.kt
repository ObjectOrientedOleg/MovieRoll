package com.objectorientedoleg.feature.genres

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.objectorientedoleg.domain.model.GenreItem
import com.objectorientedoleg.ui.components.MovieRollLoadingIndicator
import com.objectorientedoleg.ui.components.MovieRollTopAppBar
import com.objectorientedoleg.ui.components.TabLayout
import com.objectorientedoleg.ui.components.movieMediumItems
import com.objectorientedoleg.ui.theme.ThemeDefaults
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GenresRoute(
    onSearchClick: () -> Unit,
    onAccountClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenresViewModel = hiltViewModel()
) {
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GenresScreen(
        isSyncing = isSyncing,
        uiState = uiState,
        onSearchClick = onSearchClick,
        onAccountClick = onAccountClick,
        onMovieClick = onMovieClick,
        modifier = modifier
    )
}

@Composable
private fun GenresScreen(
    isSyncing: Boolean,
    uiState: GenresUiState,
    onSearchClick: () -> Unit,
    onAccountClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            GenresTopBar(
                onSearchClick = onSearchClick,
                onAccountClick = onAccountClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isSyncing) {
                MovieRollLoadingIndicator(Modifier.align(Alignment.Center))
            } else {
                GenresContent(
                    uiState = uiState,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
private fun GenresTopBar(
    onSearchClick: () -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MovieRollTopAppBar(
        modifier = modifier,
        title = stringResource(R.string.genres),
        onSearchClick = onSearchClick,
        onAccountClick = onAccountClick
    )
}

@Composable
private fun BoxScope.GenresContent(
    uiState: GenresUiState,
    onMovieClick: (String) -> Unit
) {
    when (uiState) {
        is GenresUiState.Loaded -> GenresLoadedLayout(
            genreItems = uiState.genres,
            onMovieClick = onMovieClick
        )

        is GenresUiState.Loading -> MovieRollLoadingIndicator(Modifier.align(Alignment.Center))
        is GenresUiState.NotLoaded -> {}
    }
}

@Composable
private fun GenresLoadedLayout(
    genreItems: ImmutableList<GenreItem>,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TabLayout(
        modifier = modifier.fillMaxSize(),
        tabCount = genreItems.size,
        tabTitle = { index -> genreItems[index].name },
        tabKey = { index -> genreItems[index].id }
    ) { index ->
        val moviesItem = genreItems[index].movies
        val items = moviesItem.paging.collectAsLazyPagingItems()

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(ThemeDefaults.screenEdgePadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            movieMediumItems(
                itemModifier = Modifier.fillMaxWidth(),
                movieItems = items,
                onItemClick = { onMovieClick(it.id) }
            )
        }
    }
}