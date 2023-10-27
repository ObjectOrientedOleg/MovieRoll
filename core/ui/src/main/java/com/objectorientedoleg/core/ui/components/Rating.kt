package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.objectorientedoleg.core.ui.R
import com.objectorientedoleg.core.ui.theme.*

@Composable
fun Rating(
    rating: Int,
    modifier: Modifier = Modifier,
    largeText: Boolean = false
) {
    val padding = LocalDensity.current.run { 4.dp.roundToPx() }

    SubcomposeLayout(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.background,
            shape = CircleShape
        )
    ) { constraints ->
        val indicatorMeasurables = subcompose("indicator") {
            RatingIndicator(rating)
        }
        val indicatorPlaceables = indicatorMeasurables.map { measurable ->
            measurable.measure(
                constraints.copy(
                    minWidth = 0,
                    maxWidth = constraints.maxWidth - (padding * 2),
                    minHeight = 0,
                    maxHeight = constraints.maxHeight - (padding * 2)
                )
            )
        }

        val textMeasurables = subcompose("text") {
            RatingText(
                rating = rating.toString(),
                largeText = largeText
            )
        }
        val textPlaceables = textMeasurables.map { measurable ->
            measurable.measure(constraints)
        }

        val width = constraints.maxWidth
        val height = constraints.maxHeight
        layout(width, height) {
            indicatorPlaceables.forEach { placeable ->
                placeable.placeRelative(placeable.center(width, height))
            }
            textPlaceables.forEach { placeable ->
                placeable.placeRelative(placeable.center(width, height))
            }
        }
    }
}

@Composable
private fun RatingText(
    rating: String,
    largeText: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier.wrapContentSize(),
        text = buildAnnotatedString {
            append(rating)
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )
            ) {
                append(stringResource(R.string.rating_out_of_ten))
            }
        },
        color = MaterialTheme.colorScheme.onBackground,
        style = if (largeText) {
            MaterialTheme.typography.bodyLarge
        } else {
            MaterialTheme.typography.bodySmall
        }
    )
}

@Composable
private fun RatingIndicator(rating: Int, modifier: Modifier = Modifier) {
    require(rating in 0..10) { "Unexpected rating: $rating" }
    val dashColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    val stroke = LocalDensity.current.run { Stroke(3.dp.toPx()) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        val spacing = 10f
        val sweepAngle = (360f - (spacing * 10)) / 10
        var startAngle = 270f + (spacing / 2)
        repeat(10) {
            drawArc(
                color = dashColor,
                style = stroke,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false
            )
            startAngle += (sweepAngle + spacing)
        }
        drawArc(
            color = getColorFor(rating),
            style = stroke,
            startAngle = 270f + (spacing / 2),
            sweepAngle = when (rating) {
                0 -> 0f
                10 -> 360f
                else -> (rating * (sweepAngle + spacing)) - spacing
            },
            useCenter = false
        )
    }
}

private fun getColorFor(rating: Int) = when (rating) {
    1 -> CyanAzure
    2 -> Liberty
    3 -> RoyalPurple
    4 -> Plum
    5 -> BittersweetShimmer
    6 -> DarkCoral
    7 -> Bronze
    8 -> SatinGold
    9 -> BudGreen
    10 -> PolishedPine
    else -> Color.Unspecified
}

private fun Placeable.center(layoutWidth: Int, layoutHeight: Int) =
    IntOffset(
        x = (layoutWidth / 2) - (width / 2),
        y = (layoutHeight / 2) - (height / 2)
    )

@Preview(showBackground = true)
@Composable
fun PreviewRatingZero() {
    Rating(
        modifier = Modifier.size(48.dp),
        rating = 0
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRatingTen() {
    Rating(
        modifier = Modifier.size(56.dp),
        rating = 10,
        largeText = true
    )
}