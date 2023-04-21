package com.objectorientedoleg.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

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
            color = MaterialTheme.colorScheme.primaryContainer,
            highlight = highlight
        ),
        model = imageUrl,
        contentDescription = contentDescription,
        onSuccess = { visible = false },
        onError = { error = true },
        contentScale = contentScale
    )
}