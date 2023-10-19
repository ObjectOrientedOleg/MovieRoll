package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.objectorientedoleg.core.domain.model.ImageUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumElevatedPoster(
    posterUrl: ImageUrl?,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.aspectRatio(PosterAspectRatio),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium
    ) {
        SizedAsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = posterUrl,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun MediumShimmerPoster(modifier: Modifier = Modifier) {
    ShimmerPoster(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    )
}

@Composable
fun ExtraLargePoster(
    posterUrl: ImageUrl?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.aspectRatio(PosterAspectRatio),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        SizedAsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = posterUrl,
            contentDescription = contentDescription
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtraLargeElevatedPoster(
    posterUrl: ImageUrl?,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sharpCorner: Boolean = false
) {
    ElevatedCard(
        modifier = modifier.aspectRatio(PosterAspectRatio),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge.copy(
            topStart = if (sharpCorner) {
                CornerSize(8.dp)
            } else {
                MaterialTheme.shapes.extraLarge.topStart
            }
        )
    ) {
        SizedAsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = posterUrl,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun ExtraLargeShimmerPoster(modifier: Modifier = Modifier) {
    ShimmerPoster(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    )
}

@Composable
private fun ShimmerPoster(shape: Shape, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(PosterAspectRatio),
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = true,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    highlight = PlaceholderHighlight.shimmer(Color.White)
                )
        )
    }
}

private const val PosterAspectRatio = 2f / 3