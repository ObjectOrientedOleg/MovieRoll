package com.objectorientedoleg.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.objectorientedoleg.core.domain.model.MovieItem
import com.objectorientedoleg.core.domain.model.MoviesItem
import com.objectorientedoleg.core.ui.components.MovieRollLoadingIndicator
import com.objectorientedoleg.core.ui.components.MovieRollTopBar
import com.objectorientedoleg.core.ui.components.extraLargeMovieList
import com.objectorientedoleg.core.ui.components.extraLargeShimmerItems
import com.objectorientedoleg.core.ui.theme.MovieRollTheme
import com.objectorientedoleg.core.ui.theme.ThemeDefaults
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun HomeRoute(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        uiState = uiState,
        onSearchClick = onSearchClick,
        onSettingsClick = onSettingsClick,
        onMovieClick = onMovieClick
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        MovieRollTopBar(
            title = stringResource(R.string.home_title),
            onSearchClick = onSearchClick,
            onSettingsClick = onSettingsClick
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is HomeUiState.Loaded -> HomeLoadedLayout(
                    moviesItems = uiState.movies,
                    onMovieClick = onMovieClick
                )

                is HomeUiState.Loading -> MovieRollLoadingIndicator(Modifier.align(Alignment.Center))
                is HomeUiState.NotLoaded -> {}
            }
        }
    }
}

@Composable
private fun HomeLoadedLayout(
    moviesItems: ImmutableList<MoviesItem>,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        moviesItems.forEach { moviesItem ->
            HomeMoviesRow(
                moviesItem = moviesItem,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
private fun HomeMoviesRow(
    moviesItem: MoviesItem,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val state = rememberLazyListState()
        val items = moviesItem.movies.collectAsLazyPagingItems()

        Text(
            modifier = Modifier.padding(horizontal = ThemeDefaults.screenEdgePadding),
            text = moviesItem.name,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        if (items.isLoading) {
            HomeShimmerList()
        } else {
            HomeMovieList(
                state = state,
                movieItems = items,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
private fun HomeShimmerList(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
        contentPadding = PaddingValues(
            start = ThemeDefaults.screenEdgePadding,
            top = 16.dp,
            end = ThemeDefaults.screenEdgePadding,
            bottom = ThemeDefaults.screenEdgePadding
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        extraLargeShimmerItems(Modifier.fillMaxHeight())
    }
}

@Composable
private fun HomeMovieList(
    state: LazyListState,
    movieItems: LazyPagingItems<MovieItem>,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
        state = state,
        contentPadding = PaddingValues(
            start = ThemeDefaults.screenEdgePadding,
            top = 16.dp,
            end = ThemeDefaults.screenEdgePadding,
            bottom = ThemeDefaults.screenEdgePadding
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        extraLargeMovieList(
            itemModifier = Modifier.fillMaxHeight(),
            movieItems = movieItems,
            onItemClick = { onMovieClick(it.id) }
        )
    }
}

private val LazyPagingItems<*>.isLoading
    get() = itemCount == 0 && loadState.refresh is LoadState.Loading

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "light theme"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "dark theme"
)
@Composable
fun PreviewHomeScreen() {
    MovieRollTheme {
        //HomeScreen()
    }
}