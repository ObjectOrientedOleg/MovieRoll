package com.objectorientedoleg.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.objectorientedoleg.domain.model.ImageUrl

@Composable
fun AsyncImageWithLoadingShimmer(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var visible by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }
    val highlight = if (visible && !error) {
        PlaceholderHighlight.shimmer(Color.White)
    } else {
        null
    }

    AsyncImage(
        modifier = modifier.placeholder(
            visible = visible,
            color = MaterialTheme.colorScheme.surfaceVariant,
            highlight = highlight
        ),
        model = imageUrl,
        contentDescription = contentDescription,
        onSuccess = { visible = false },
        onError = { error = true },
        contentScale = contentScale
    )
}

@Composable
fun SizedAsyncImage(
    imageUrl: ImageUrl?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val imageSize = LocalDensity.current.run { maxWidth.roundToPx() }

        AsyncImageWithLoadingShimmer(
            modifier = Modifier.fillMaxSize(),
            imageUrl = remember(imageSize) {
                imageUrl?.invoke(imageSize)
            },
            contentDescription = contentDescription
        )
    }
}