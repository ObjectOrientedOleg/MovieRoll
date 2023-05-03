package com.objectorientedoleg.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.objectorientedoleg.domain.model.DiscoverMovie

fun LazyListScope.discoverMovieItems(
    discoverMovies: LazyPagingItems<DiscoverMovie>,
    modifier: Modifier = Modifier
) {
    itemsIndexed(
        items = discoverMovies,
        key = { _, discoverMovie -> discoverMovie.id }
    ) { index, discoverMovie ->
        if (discoverMovie != null) {
            DiscoverMovieItem(
                modifier = modifier,
                discoverMovie = discoverMovie,
                posterOnLeft = index % 2 == 0
            )
        }
    }
}

@Composable
fun DiscoverMovieItem(
    discoverMovie: DiscoverMovie,
    posterOnLeft: Boolean,
    modifier: Modifier = Modifier
) {
    PosterWithDetail(
        modifier = modifier,
        containerColor = discoverMovie.colorPalette?.backgroundColor
            ?: MaterialTheme.colorScheme.surfaceVariant,
        posterOnLeft = posterOnLeft,
        poster = {
            AsyncImageWithLoadingShimmer(
                modifier = Modifier.fillMaxSize(),
                imageUrl = discoverMovie.posterUrl,
                contentDescription = null
            )
        },
        detail = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = discoverMovie.title,
                    color = discoverMovie.colorPalette?.titleTextColor
                        ?: MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    )
}