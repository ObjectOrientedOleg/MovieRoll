package com.objectorientedoleg.ui.theme

import androidx.annotation.RawRes
import androidx.compose.runtime.staticCompositionLocalOf
import com.objectorientedoleg.core.ui.R

data class LoadingIndicatorResource(@RawRes val id: Int = 0) {

    companion object {
        fun fromTheme(darkTheme: Boolean): LoadingIndicatorResource =
            LoadingIndicatorResource(
                if (darkTheme) R.raw.movieroll_loading_light else R.raw.movieroll_loading_dark
            )
    }
}

val LocalLoadingIndicatorResource = staticCompositionLocalOf { LoadingIndicatorResource() }