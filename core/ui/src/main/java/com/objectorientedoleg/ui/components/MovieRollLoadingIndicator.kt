package com.objectorientedoleg.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.objectorientedoleg.core.ui.R

@Composable
fun MovieRollLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            if (darkTheme) R.raw.movieroll_loading_light else R.raw.movieroll_loading_dark
        )
    )

    LottieAnimation(
        modifier = modifier.sizeIn(maxWidth = size, maxHeight = size),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}