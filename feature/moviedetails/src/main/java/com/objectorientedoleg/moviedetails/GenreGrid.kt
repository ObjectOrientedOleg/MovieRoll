package com.objectorientedoleg.moviedetails

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.objectorientedoleg.domain.model.MovieDetailsItem.Genre
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GenreGrid(
    genreItems: ImmutableList<Genre>,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contents: List<@Composable () -> Unit> = genreItems.map { genre ->
        @Composable {
            GenreItem(genre = genre, onGenreClick = onGenreClick)
        }
    }
    val padding = LocalDensity.current.run { 8.dp.roundToPx() }

    Layout(
        modifier = modifier,
        contents = contents
    ) { measurables, constraints ->
        val emptyConstraints = Constraints()
        val placeables = measurables.map { list ->
            list[0].measure(emptyConstraints)
        }

        var width = 0
        val height = placeables.foldIndexed(0) { index, height, placeable ->
            val placeableWidth = placeable.width + if (index == placeables.lastIndex) 0 else padding
            width += placeableWidth
            if (width > constraints.maxWidth) {
                width = placeableWidth
                height + placeable.height + (padding / 4)
            } else {
                maxOf(placeable.height, height)
            }
        }
        val maxHeight = minOf(constraints.maxHeight, height)

        layout(constraints.maxWidth, maxHeight) {
            var x = 0
            var y = 0
            placeables.forEachIndexed { index, placeable ->
                val placeableWidth =
                    placeable.width + if (index == placeables.lastIndex) 0 else padding
                if ((x + placeableWidth) > constraints.maxWidth) {
                    x = 0
                    y += (placeable.height + (padding / 4))
                }
                placeable.place(x, y)
                x += placeableWidth
            }
        }
    }
}

@Composable
private fun GenreItem(
    genre: Genre,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = { onGenreClick(genre.id) }
    ) {
        Text(genre.name)
    }
}