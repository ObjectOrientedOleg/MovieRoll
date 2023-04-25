package com.objectorientedoleg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private const val PosterAspectRatio = 2f / 3
private val MaterialThemeElevationLevel2 = 3.dp

@Composable
fun PosterWithDetail(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    posterOnLeft: Boolean = true,
    poster: @Composable () -> Unit,
    detail: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val detailWidth = maxWidth - (maxHeight * PosterAspectRatio)
        val detailHeight = maxHeight - (MaterialTheme.shapes.medium.cornerSize() * 2)
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (posterOnLeft) {
                PosterWithShadow(
                    modifier = Modifier.fillMaxHeight(),
                    shadowColor = containerColor,
                    content = poster
                )
                Box(
                    modifier = Modifier
                        .width(detailWidth)
                        .height(detailHeight)
                        .background(
                            color = containerColor,
                            shape = MaterialTheme.shapes.medium.copy(
                                topStart = ZeroCornerSize,
                                bottomStart = ZeroCornerSize
                            )
                        )
                ) {
                    detail()
                }
            } else {
                Box(
                    modifier = Modifier
                        .width(detailWidth)
                        .height(detailHeight)
                        .background(
                            color = containerColor,
                            shape = MaterialTheme.shapes.medium.copy(
                                topEnd = ZeroCornerSize,
                                bottomEnd = ZeroCornerSize
                            )
                        )
                ) {
                    detail()
                }
                PosterWithShadow(
                    modifier = Modifier.fillMaxHeight(),
                    shadowColor = containerColor,
                    content = poster
                )
            }
        }
    }
}

@Composable
private fun CornerBasedShape.cornerSize() = with(LocalDensity.current) {
    topStart.toPx(Size.Unspecified, this).toDp()
}

@Composable
fun PosterWithShadow(
    modifier: Modifier = Modifier,
    shadowColor: Color = DefaultShadowColor,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(PosterAspectRatio)
            .shadow(
                elevation = MaterialThemeElevationLevel2,
                shape = MaterialTheme.shapes.medium,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(MaterialTheme.shapes.medium)
    ) {
        content()
    }
}