package com.objectorientedoleg.moviedetails

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.objectorientedoleg.domain.model.ImageUrl
import com.objectorientedoleg.domain.model.MovieDetailsItem
import com.objectorientedoleg.ui.components.ExtraLargePoster
import com.objectorientedoleg.ui.components.MovieRollLoadingIndicator
import com.objectorientedoleg.ui.theme.ThemeDefaults
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MovieDetailsRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieDetailsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onBookmarkClick = {},
        onGenreClick = {}
    )
}

@Composable
private fun MovieDetailsScreen(
    uiState: MovieDetailsUiState,
    onBackClick: () -> Unit,
    onBookmarkClick: (String) -> Unit,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MovieDetailsTopBar(uiState.title, onBackClick) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MovieDetailsLayout(
                uiState = uiState,
                onBookmarkClick = onBookmarkClick,
                onGenreClick = onGenreClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(com.objectorientedoleg.core.ui.R.string.back)
                )
            }
        }
    )
}

@Composable
private fun BoxScope.MovieDetailsLayout(
    uiState: MovieDetailsUiState,
    onBookmarkClick: (String) -> Unit,
    onGenreClick: (String) -> Unit
) {
    when (uiState) {
        is MovieDetailsUiState.Loaded -> MovieDetailsLoadedLayout(
            movieDetailsItem = uiState.movieDetails,
            onBookmarkClick = onBookmarkClick,
            onGenreClick = onGenreClick
        )

        is MovieDetailsUiState.Loading -> MovieRollLoadingIndicator(Modifier.align(Alignment.Center))
        is MovieDetailsUiState.NotLoaded -> {}
    }
}

@Composable
private fun MovieDetailsLoadedLayout(
    movieDetailsItem: MovieDetailsItem,
    onBookmarkClick: (String) -> Unit,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        MovieDetailsPoster(
            movieDetailsItem = movieDetailsItem,
            onBookmarkClick = onBookmarkClick
        )
        Spacer(Modifier.height(16.dp))
        val tagline = movieDetailsItem.tagline
        if (tagline != null) {
            MovieDetailsTagline(tagline)
            Spacer(Modifier.height(16.dp))
        }
        val overview = movieDetailsItem.overview
        if (overview != null) {
            MovieDetailsAbout(overview)
            Spacer(Modifier.height(16.dp))
        }
        MovieDetailsReleaseDate(movieDetailsItem.releaseDate)
        Spacer(Modifier.height(16.dp))
        if (movieDetailsItem.genres.isNotEmpty()) {
            MovieDetailsGenres(
                genreItems = movieDetailsItem.genres,
                onGenreClick = onGenreClick
            )
            Spacer(Modifier.height(16.dp))
        }
        if (movieDetailsItem.credits.cast.isNotEmpty()) {
            MovieDetailsCredits(
                title = stringResource(R.string.cast),
                creditItems = movieDetailsItem.credits.cast
            )
            Spacer(Modifier.height(16.dp))
        }
        if (movieDetailsItem.credits.crew.isNotEmpty()) {
            MovieDetailsCredits(
                title = stringResource(R.string.crew),
                creditItems = movieDetailsItem.credits.crew
            )
            Spacer(Modifier.height(16.dp))
        }
        if (movieDetailsItem.backdropUrls.isNotEmpty()) {
            MovieDetailsBackdrops(movieDetailsItem.backdropUrls)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MovieDetailsPoster(
    movieDetailsItem: MovieDetailsItem,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThemeDefaults.screenEdgePadding),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            ExtraLargePoster(
                modifier = Modifier.weight(1f),
                posterUrl = movieDetailsItem.posterUrl,
                contentDescription = movieDetailsItem.title
            )
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .fillMaxHeight()
                    .padding(ThemeDefaults.screenEdgePadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val certification = movieDetailsItem.certification
                if (certification != null) {
                    MovieDetailsCertification(certification)
                    Divider(Modifier.fillMaxWidth())
                }
                val runtime = movieDetailsItem.runtime
                if (runtime != null) {
                    Text(
                        text = runtime,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Divider(Modifier.fillMaxWidth())
                }
                Text(
                    text = buildAnnotatedString {
                        append(movieDetailsItem.voteAverage)
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = MaterialTheme.typography.labelSmall.fontSize
                            )
                        ) {
                            append(stringResource(com.objectorientedoleg.core.ui.R.string.rating_out_of_ten))
                        }
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                Divider(Modifier.fillMaxWidth())
                IconButton(
                    onClick = { onBookmarkClick(movieDetailsItem.id) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.BookmarkBorder,
                        contentDescription = stringResource(R.string.bookmark)
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsCertification(certification: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = certification,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun MovieDetailsTagline(tagline: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThemeDefaults.screenEdgePadding)
    ) {
        Text(
            text = tagline,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun MovieDetailsAbout(overview: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThemeDefaults.screenEdgePadding)
    ) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = overview,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp)
        )
    }
}

@Composable
private fun MovieDetailsReleaseDate(releaseDate: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThemeDefaults.screenEdgePadding)
    ) {
        Text(
            text = stringResource(R.string.release_date),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = releaseDate,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun MovieDetailsGenres(
    genreItems: ImmutableList<MovieDetailsItem.Genre>,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThemeDefaults.screenEdgePadding)
    ) {
        Text(
            text = stringResource(R.string.genres),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        GenreGrid(
            modifier = Modifier.fillMaxWidth(),
            genreItems = genreItems,
            onGenreClick = onGenreClick
        )
    }
}

@Composable
private fun MovieDetailsCredits(
    title: String,
    creditItems: ImmutableList<MovieDetailsItem.Credits.Credit>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = ThemeDefaults.screenEdgePadding),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp),
            contentPadding = PaddingValues(horizontal = ThemeDefaults.screenEdgePadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            creditList(
                itemModifier = Modifier
                    .widthIn(max = 125.dp)
                    .fillMaxHeight(),
                creditItems = creditItems
            )
        }
    }
}

@Composable
private fun MovieDetailsBackdrops(
    backdropUrls: ImmutableList<ImageUrl>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ThemeDefaults.screenEdgePadding)
    ) {
        Text(
            text = stringResource(R.string.images),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        BackdropPager(
            modifier = Modifier.fillMaxWidth(),
            backdropUrls = backdropUrls
        )
    }
}