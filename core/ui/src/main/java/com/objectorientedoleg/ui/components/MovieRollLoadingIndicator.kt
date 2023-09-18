package com.objectorientedoleg.ui.components

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
import com.objectorientedoleg.ui.theme.LocalLoadingIndicatorResource

@Composable
fun MovieRollLoadingIndicator(modifier: Modifier = Modifier, size: Dp = 48.dp) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(LocalLoadingIndicatorResource.current.id)
    )

    LottieAnimation(
        modifier = modifier.sizeIn(maxWidth = size, maxHeight = size),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}