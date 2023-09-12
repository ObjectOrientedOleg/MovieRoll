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
import androidx.compose.material3.CircularProgressIndicator
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
import com.objectorientedoleg.ui.components.TabLayout
import com.objectorientedoleg.ui.components.discoverMovieExtraLargeItems
import com.objectorientedoleg.ui.components.discoverMovieMediumItems
import com.objectorientedoleg.ui.theme.MovieRollTheme
import com.objectorientedoleg.ui.theme.ThemeDefaults

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(uiState: HomeUiState, modifier: Modifier = Modifier) {
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
                HomeTabLayout(uiState.discoverGenresUiState)
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
private fun HomeTabLayout(uiState: DiscoverGenresUiState, modifier: Modifier = Modifier) {
    when (uiState) {
        is DiscoverGenresUiState.Loaded -> TabLayout(
            modifier = modifier.fillMaxSize(),
            tabCount = uiState.genres.size,
            tabTitle = { index -> uiState.genres[index].name },
            tabKey = { index -> uiState.genres[index].id }
        ) { index ->
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(ThemeDefaults.screenEdgePadding))
                when (val discoverGenre = uiState.genres[index]) {
                    is DiscoverGenre.CombinedGenres -> CustomGenresTab(discoverGenre.genres)
                    is DiscoverGenre.SingleGenre -> SingleGenreTab(discoverGenre.movies)
                }
            }
        }

        is DiscoverGenresUiState.Loading -> HomeLoadingIndicator()
        is DiscoverGenresUiState.NotLoaded -> {}
    }
}

@Composable
private fun CustomGenresTab(
    genres: List<DiscoverGenre.SingleGenre>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        genres.forEach { genre ->
            CustomGenreRow(genre)
        }
    }
}

@Composable
private fun CustomGenreRow(genre: DiscoverGenre.SingleGenre, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        val movies = genre.movies.paging.collectAsLazyPagingItems()

        Text(
            modifier = Modifier.padding(horizontal = ThemeDefaults.screenEdgePadding),
            text = genre.name,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
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
                discoverMovies = movies,
                onItemClick = {}
            )
        }
    }
}

@Composable
private fun SingleGenreTab(discoverMovies: DiscoverMovies, modifier: Modifier = Modifier) {
    val movies = discoverMovies.paging.collectAsLazyPagingItems()

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
            discoverMovies = movies,
            onItemClick = {}
        )
    }
}

@Composable
private fun HomeLoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
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