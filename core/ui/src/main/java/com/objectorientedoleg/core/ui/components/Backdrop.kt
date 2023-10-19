package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.objectorientedoleg.core.domain.model.ImageUrl

@Composable
fun Backdrop(
    backdropUrl: ImageUrl?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    SizedAsyncImage(
        modifier = modifier.aspectRatio(BackdropAspectRatio),
        imageUrl = backdropUrl,
        contentDescription = contentDescription
    )
}

private const val BackdropAspectRatio = 16f / 9