package com.objectorientedoleg.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.objectorientedoleg.domain.model.DiscoverGenre
import com.objectorientedoleg.domain.model.DiscoverMovies
import com.objectorientedoleg.ui.components.MovieRollLoadingIndicator
import com.objectorientedoleg.ui.components.TabLayout
import com.objectorientedoleg.ui.components.discoverMovieExtraLargeItems
import com.objectorientedoleg.ui.components.discoverMovieMediumItems
import com.objectorientedoleg.ui.theme.MovieRollTheme
import com.objectorientedoleg.ui.theme.ThemeDefaults

@Composable
internal fun HomeRoute(
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        uiState = uiState,
        onMovieClick = onMovieClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HomeTopBar(scrollBehavior) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isSyncing) {
                HomeLoadingIndicator()
            } else {
                Spacer(modifier = Modifier.height(ThemeDefaults.screenEdgePadding))
                HomeLayout(uiState.discoverGenresUiState, onMovieClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.home_title),
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            Icon(
                modifier = Modifier.padding(horizontal = ThemeDefaults.appBarPadding),
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_content_desc)
            )
        },
        actions = {
            Icon(
                modifier = Modifier.padding(horizontal = ThemeDefaults.appBarPadding),
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(R.string.account_content_desc)
            )
        }
    )
}

@Composable
private fun HomeLayout(
    uiState: DiscoverGenresUiState,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is DiscoverGenresUiState.Loaded -> HomeTabLayout(
            modifier = modifier,
            discoverGenres = uiState.genres,
            onMovieClick = onMovieClick
        )

        is DiscoverGenresUiState.Loading -> HomeLoadingIndicator()
        is DiscoverGenresUiState.NotLoaded -> {}
    }
}

@Composable
private fun HomeTabLayout(
    discoverGenres: List<DiscoverGenre>,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TabLayout(
        modifier = modifier.fillMaxSize(),
        tabCount = discoverGenres.size,
        tabTitle = { index -> discoverGenres[index].name },
        tabKey = { index -> discoverGenres[index].id }
    ) { index ->
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(ThemeDefaults.screenEdgePadding))
            when (val discoverGenre = discoverGenres[index]) {
                is DiscoverGenre.CombinedGenres -> CustomGenresTab(
                    discoverGenre.genres,
                    onMovieClick
                )

                is DiscoverGenre.SingleGenre -> SingleGenreTab(
                    discoverGenre.movies,
                    onMovieClick
                )
            }
        }
    }
}

@Composable
private fun CustomGenresTab(
    genres: List<DiscoverGenre.SingleGenre>,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        genres.forEach { genre ->
            CustomGenreRow(genre, onMovieClick)
        }
    }
}

@Composable
private fun CustomGenreRow(
    genre: DiscoverGenre.SingleGenre,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = ThemeDefaults.screenEdgePadding),
            text = genre.name,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        CustomGenreMovies(genre.movies, onMovieClick)
    }
}

@Composable
private fun CustomGenreMovies(
    discoverMovies: DiscoverMovies,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyPagingItems = discoverMovies.paging.collectAsLazyPagingItems()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = ThemeDefaults.screenEdgePadding,
            top = 12.dp,
            end = ThemeDefaults.screenEdgePadding,
            bottom = ThemeDefaults.screenEdgePadding
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        discoverMovieExtraLargeItems(
            itemModifier = Modifier.width(250.dp),
            discoverMovies = lazyPagingItems,
            onItemClick = { discoverMovie -> onMovieClick(discoverMovie.id.toString()) }
        )
    }
}

@Composable
private fun SingleGenreTab(
    discoverMovies: DiscoverMovies,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyPagingItems = discoverMovies.paging.collectAsLazyPagingItems()

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = ThemeDefaults.screenEdgePadding,
            top = 0.dp,
            end = ThemeDefaults.screenEdgePadding,
            bottom = ThemeDefaults.screenEdgePadding
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        discoverMovieMediumItems(
            itemModifier = Modifier.fillMaxWidth(),
            discoverMovies = lazyPagingItems,
            onItemClick = { discoverMovie -> onMovieClick(discoverMovie.id.toString()) }
        )
    }
}

@Composable
private fun HomeLoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MovieRollLoadingIndicator()
    }
}

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