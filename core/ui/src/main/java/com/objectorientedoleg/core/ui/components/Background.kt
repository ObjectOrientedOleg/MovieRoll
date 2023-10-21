package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.objectorientedoleg.core.ui.theme.GradientColors
import com.objectorientedoleg.core.ui.theme.LocalGradientColors
import kotlin.math.tan

// Copied from https://github.com/android/nowinandroid/blob/main/core/designsystem/src/main/kotlin/com/google/samples/apps/nowinandroid/core/designsystem/component/Background.kt
@Composable
fun MovieRollGradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    content: @Composable () -> Unit
) {
    val topColor by rememberUpdatedState(gradientColors.top)
    val bottomColor by rememberUpdatedState(gradientColors.bottom)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = if (gradientColors.container == Color.Unspecified) {
            Color.Transparent
        } else {
            gradientColors.container
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    val offset = size.height * tan(
                        Math
                            .toRadians(11.06)
                            .toFloat(),
                    )

                    val start = Offset(size.width / 2 + offset / 2, 0f)
                    val end = Offset(size.width / 2 - offset / 2, size.height)

                    val topGradient = Brush.linearGradient(
                        0f to if (topColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            topColor
                        },
                        0.724f to Color.Transparent,
                        start = start,
                        end = end,
                    )
                    val bottomGradient = Brush.linearGradient(
                        0.2552f to Color.Transparent,
                        1f to if (bottomColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            bottomColor
                        },
                        start = start,
                        end = end,
                    )

                    onDrawBehind {
                        drawRect(topGradient)
                        drawRect(bottomGradient)
                    }
                },
        ) {
            content()
        }
    }
}