package com.objectorientedoleg.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.objectorientedoleg.core.ui.theme.LocalLoadingIndicatorResource

@Composable
fun MovieRollLoadingIndicator(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(LocalLoadingIndicatorResource.current.id)
    )

    LottieAnimation(
        modifier = modifier.size(LoadingIndicatorDefaultSize),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

private val LoadingIndicatorDefaultSize = 48.dp