package com.objectorientedoleg.feature.moviedetails

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
import com.objectorientedoleg.core.domain.model.ImageUrl
import com.objectorientedoleg.core.domain.model.MovieDetailsItem
import com.objectorientedoleg.core.ui.components.ExtraLargePoster
import com.objectorientedoleg.core.ui.components.MovieRollLoadingIndicator
import com.objectorientedoleg.core.ui.theme.ThemeDefaults
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
        Spacer(Modifier.height(ThemeDefaults.screenEdgePadding))
        MovieDetailsPoster(
            movieDetailsItem = movieDetailsItem,
            onBookmarkClick = onBookmarkClick
        )
        Spacer(Modifier.height(16.dp))
        val tagline = movieDetailsItem.tagline
        if (!tagline.isNullOrEmpty()) {
            MovieDetailsTagline(tagline)
            Spacer(Modifier.height(16.dp))
        }
        val overview = movieDetailsItem.overview
        if (!overview.isNullOrEmpty()) {
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
            Spacer(Modifier.height(ThemeDefaults.screenEdgePadding))
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
                    .padding(
                        horizontal = 16.dp,
                        vertical = 32.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val certification = movieDetailsItem.certification
                if (!certification.isNullOrEmpty()) {
                    MovieDetailsCertification(certification)
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                val runtime = movieDetailsItem.runtime
                if (!runtime.isNullOrEmpty()) {
                    Text(
                        text = runtime,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outline
                    )
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
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outline
                )
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
            fontWeight = FontWeight.Bold,
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
            fontWeight = FontWeight.Bold,
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
            fontWeight = FontWeight.Bold,
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
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp),
            contentPadding = PaddingValues(horizontal = ThemeDefaults.screenEdgePadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            creditList(
                itemModifier = Modifier.fillMaxHeight(),
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
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        BackdropPager(
            modifier = Modifier.fillMaxWidth(),
            backdropUrls = backdropUrls
        )
    }
}