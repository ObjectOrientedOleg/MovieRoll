package com.objectorientedoleg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun Rating(
    rating: Double,
    modifier: Modifier = Modifier,
    largeText: Boolean = false
) {
    val strokeWidth = 2.dp
    val strokeSize = LocalDensity.current.run { strokeWidth.roundToPx() }
    val padding = LocalDensity.current.run { 4.dp.roundToPx() }

    SubcomposeLayout(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = CircleShape
        )
    ) { constraints ->
        val textMeasurables = subcompose("text") {
            Text(
                text = buildAnnotatedString {
                    append(rating.roundToInt().toString())
                    withStyle(
                        SpanStyle(
                            fontSize = 8.sp,
                            baselineShift = BaselineShift.Superscript
                        )
                    ) {
                        append("/10")
                    }
                },
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = if (largeText) {
                    MaterialTheme.typography.bodyLarge
                } else {
                    MaterialTheme.typography.bodySmall
                }
            )
        }
        val textPlaceables = textMeasurables.map { measurable ->
            measurable.measure(constraints)
        }
        val textSize = textPlaceables.fold(IntSize.Zero) { size, placeable ->
            IntSize(
                width = maxOf(size.width, placeable.width),
                height = maxOf(size.height, placeable.height)
            )
        }

        val progressMeasurables = subcompose("progress") {
            CircularProgressIndicator(
                progress = (rating / 10).toFloat(),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                strokeWidth = strokeWidth
            )
        }
        val progressSize = textSize.max() + (strokeSize * 2) + (padding * 2)
        val progressPlaceables = progressMeasurables.map { measurable ->
            measurable.measure(Constraints.fixed(progressSize, progressSize))
        }

        val layoutSize = progressSize + (padding * 2)
        layout(layoutSize, layoutSize) {
            progressPlaceables.forEach { placeable ->
                placeable.placeRelative(placeable.center(layoutSize))
            }
            textPlaceables.forEach { placeable ->
                placeable.placeRelative(placeable.center(layoutSize))
            }
        }
    }
}

private fun IntSize.max() = maxOf(width, height)

private fun Placeable.center(diameter: Int): IntOffset {
    val centerPos = diameter / 2
    val xPos = centerPos - (width / 2)
    val yPos = centerPos - (height / 2)
    return IntOffset(xPos, yPos)
}

@Preview(showBackground = true)
@Composable
fun PreviewRating() {
    Rating(rating = 10.0, largeText = true)
}