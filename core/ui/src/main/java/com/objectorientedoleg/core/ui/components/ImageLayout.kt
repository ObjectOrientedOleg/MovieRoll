package com.objectorientedoleg.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@Composable
fun ImageTextLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val padding = LocalDensity.current.run { 12.dp.roundToPx() }

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 2) { "Expected 2 children, was ${measurables.size}" }
        check(constraints.maxWidth == Int.MAX_VALUE) { "Unexpected constraints: $constraints" }

        val textHeight = measurables[1].minIntrinsicHeight(constraints.maxWidth)
        val imagePlaceable = measurables[0].measure(
            constraints.copy(
                minHeight = 0,
                maxHeight = constraints.maxHeight - (textHeight + padding)
            )
        )
        val textPlaceable = measurables[1].measure(
            constraints.copy(
                minWidth = 0,
                maxWidth = imagePlaceable.width,
                minHeight = 0,
                maxHeight = constraints.maxHeight - imagePlaceable.height - padding
            )
        )
        val width = minOf(constraints.maxWidth, imagePlaceable.width)
        val height = minOf(
            a = constraints.maxHeight,
            b = imagePlaceable.height + textPlaceable.height + padding
        )
        layout(width, height) {
            imagePlaceable.placeRelative(0, 0)
            textPlaceable.placeRelative(
                x = (width - textPlaceable.width) / 2,
                y = imagePlaceable.height + padding
            )
        }
    }
}

@Composable
fun ImageDecorationLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val padding = LocalDensity.current.run { 12.dp.roundToPx() }

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 2) { "Expected 2 children, was ${measurables.size}" }
        when {
            constraints.maxWidth == Int.MAX_VALUE -> measureUnconstrainedWidthResult(
                measurables = measurables,
                constraints = constraints,
                padding = padding
            )

            constraints.maxHeight == Int.MAX_VALUE -> measureUnconstrainedHeightResult(
                measurables = measurables,
                constraints = constraints,
                padding = padding
            )

            else -> error("Unexpected constraints: $constraints")
        }
    }
}

private fun MeasureScope.measureUnconstrainedWidthResult(
    measurables: List<Measurable>,
    constraints: Constraints,
    padding: Int
): MeasureResult {
    val decorationPlaceable = measurables[1].measure(constraints)
    val decorationHeight = decorationPlaceable.height / 2
    val imagePlaceable = measurables[0].measure(
        constraints.copy(
            minHeight = 0,
            maxHeight = constraints.maxHeight - decorationHeight
        )
    )
    val width = minOf(constraints.maxWidth, imagePlaceable.width)
    val height = minOf(
        a = constraints.maxHeight,
        b = imagePlaceable.height + decorationHeight
    )
    return layout(width, height) {
        imagePlaceable.placeRelative(0, 0)
        decorationPlaceable.placeRelative(
            x = padding,
            y = imagePlaceable.height - decorationHeight
        )
    }
}

private fun MeasureScope.measureUnconstrainedHeightResult(
    measurables: List<Measurable>,
    constraints: Constraints,
    padding: Int
): MeasureResult {
    val placeables = measurables.map { measurable ->
        measurable.measure(constraints)
    }
    val imagePlaceable = placeables[0]
    val decorationPlaceable = placeables[1]
    val width = minOf(constraints.maxWidth, imagePlaceable.width)
    val height = minOf(
        a = constraints.maxHeight,
        b = imagePlaceable.height + (decorationPlaceable.height / 2)
    )
    return layout(width, height) {
        imagePlaceable.placeRelative(0, 0)
        decorationPlaceable.placeRelative(
            x = padding,
            y = imagePlaceable.height - (decorationPlaceable.height / 2)
        )
    }
}