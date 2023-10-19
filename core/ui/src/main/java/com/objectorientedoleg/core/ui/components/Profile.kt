package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.objectorientedoleg.core.domain.model.ImageUrl

@Composable
fun CircularElevatedProfile(
    profileUrl: ImageUrl?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.aspectRatio(ProfileAspectRatio),
        shape = CircleShape
    ) {
        SizedAsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = profileUrl,
            contentDescription = contentDescription
        )
    }
}

private const val ProfileAspectRatio = 1f