package com.objectorientedoleg.core.ui.components

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.objectorientedoleg.core.domain.model.MovieItem

fun LazyListScope.extraLargeMovieList(
    movieItems: LazyPagingItems<MovieItem>,
    onItemClick: (MovieItem) -> Unit,
    itemModifier: Modifier = Modifier
) {
    items(
        items = movieItems,
        key = { movieItem -> movieItem.id }
    ) { index, movieItem ->
        if (movieItem != null) {
            MovieExtraLargeItem(
                modifier = itemModifier,
                movieItem = movieItem,
                isFirstItem = index == 0,
                onClick = onItemClick
            )
        }
    }
    movieItems.run {
        when {
            loadState.refresh is LoadState.Loading -> extraLargeShimmerItems(itemModifier)

            loadState.append is LoadState.Loading -> item {
                SpinnerItem()
            }
        }
    }
}

fun LazyListScope.extraLargeShimmerItems(itemModifier: Modifier = Modifier) {
    repeat(2) {
        item { ExtraLargeShimmerPoster(modifier = itemModifier) }
    }
}

@Composable
private fun MovieExtraLargeItem(
    movieItem: MovieItem,
    isFirstItem: Boolean,
    onClick: (MovieItem) -> Unit,
    modifier: Modifier = Modifier
) {
    ImageTextLayout(modifier = modifier) {
        ImageDecorationLayout {
            ExtraLargeElevatedPoster(
                modifier = Modifier.fillMaxSize(),
                posterUrl = movieItem.posterUrl,
                contentDescription = movieItem.title,
                sharpCorner = isFirstItem,
                onClick = { onClick(movieItem) }
            )
            Rating(
                rating = movieItem.voteAverage,
                largeText = true
            )
        }
        Text(
            text = buildAnnotatedString {
                append(movieItem.title)
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(" (")
                    append(movieItem.releaseYear.toString())
                    append(")")
                }
            },
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

fun LazyGridScope.mediumMovieGrid(
    movieItems: LazyPagingItems<MovieItem>,
    onItemClick: (MovieItem) -> Unit,
    itemModifier: Modifier = Modifier
) {
    items(
        items = movieItems,
        key = { movieItem -> movieItem.id }
    ) { movieItem ->
        if (movieItem != null) {
            MovieMediumItem(
                modifier = itemModifier,
                movieItem = movieItem,
                onClick = onItemClick
            )
        }
    }
    movieItems.run {
        when {
            loadState.refresh is LoadState.Loading -> mediumShimmerGrid(itemModifier)

            loadState.append is LoadState.Loading -> item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                SpinnerItem()
            }
        }
    }
}

fun LazyGridScope.mediumShimmerGrid(itemModifier: Modifier = Modifier) {
    repeat(6) {
        item { MediumShimmerPoster(modifier = itemModifier) }
    }
}

@Composable
private fun MovieMediumItem(
    movieItem: MovieItem,
    onClick: (MovieItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ImageDecorationLayout {
            MediumElevatedPoster(
                modifier = Modifier.fillMaxSize(),
                posterUrl = movieItem.posterUrl,
                contentDescription = movieItem.title,
                onClick = { onClick(movieItem) }
            )
            Rating(movieItem.voteAverage)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = buildAnnotatedString {
                append(movieItem.title)
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(" (")
                    append(movieItem.releaseYear.toString())
                    append(")")
                }
            },
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Composable
private fun SpinnerItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/*
Copy of Paging Compose LazyListScope.items(...).
 */
private fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(index: Int, item: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = if (key == null) null else { index ->
            val item = items.peek(index)
            if (item == null) {
                PagingPlaceholderKey(index)
            } else {
                key(item)
            }
        }
    ) { index ->
        itemContent(index, items[index])
    }
}

private fun <T : Any> LazyGridScope.items(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyGridItemScope.(item: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = if (key == null) null else { index ->
            val item = items.peek(index)
            if (item == null) {
                PagingPlaceholderKey(index)
            } else {
                key(item)
            }
        }
    ) { index ->
        itemContent(items[index])
    }
}

@SuppressLint("BanParcelableUsage")
private data class PagingPlaceholderKey(private val index: Int) : Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<PagingPlaceholderKey> =
            object : Parcelable.Creator<PagingPlaceholderKey> {
                override fun createFromParcel(parcel: Parcel) =
                    PagingPlaceholderKey(parcel.readInt())

                override fun newArray(size: Int) = arrayOfNulls<PagingPlaceholderKey?>(size)
            }
    }
}