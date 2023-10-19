package com.objectorientedoleg.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.objectorientedoleg.domain.model.ImageUrl

@Composable
fun AsyncImageWithLoadingShimmer(
    imageUrl: String?,
    imageWidth: Int,
    imageHeight: Int,
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
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(imageWidth, imageHeight)
            .build(),
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
        val imageWidth = LocalDensity.current.run { maxWidth.roundToPx() }
        val imageHeight = LocalDensity.current.run { maxHeight.roundToPx() }

        AsyncImageWithLoadingShimmer(
            modifier = Modifier.fillMaxSize(),
            imageUrl = remember(imageUrl, imageWidth) {
                imageUrl?.invoke(imageWidth)
            },
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            contentDescription = contentDescription
        )
    }
}