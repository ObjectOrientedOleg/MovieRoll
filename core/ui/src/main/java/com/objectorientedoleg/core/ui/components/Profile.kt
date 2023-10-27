package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.objectorientedoleg.core.domain.model.ImageUrl

@Composable
fun SmallProfile(
    profileUrl: ImageUrl?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.aspectRatio(ProfileAspectRatio),
        shape = MaterialTheme.shapes.small
    ) {
        SizedAsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = profileUrl,
            contentDescription = contentDescription
        )
    }
}

private const val ProfileAspectRatio = 1f