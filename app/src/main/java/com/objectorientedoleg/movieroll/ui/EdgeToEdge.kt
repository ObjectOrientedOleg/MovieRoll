package com.objectorientedoleg.movieroll.ui

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge

fun ComponentActivity.enableEdgeToEdge(darkTheme: Boolean) {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            Color.TRANSPARENT,
            Color.TRANSPARENT,
        ) { darkTheme },
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim,
            darkScrim,
        ) { darkTheme }
    )
}

// The default light scrim, as defined by androidx and the platform:
// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

// The default dark scrim, as defined by androidx and the platform:
// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)