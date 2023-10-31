package com.objectorientedoleg.movieroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.objectorientedoleg.core.data.sync.SyncManager
import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.ui.theme.MovieRollTheme
import com.objectorientedoleg.movieroll.ui.MovieRollApp
import com.objectorientedoleg.movieroll.ui.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var syncManager: SyncManager

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val darkTheme = shouldUseDarkTheme(uiState.config)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(darkTheme)
                onDispose {}
            }

            MovieRollTheme(
                darkTheme = darkTheme,
                dynamicColor = uiState.dynamicColor
            ) {
                MovieRollApp(syncManager)
            }
        }
    }
}

@Composable
private fun shouldUseDarkTheme(config: DarkThemeConfig) = when (config) {
    DarkThemeConfig.FollowSystem -> isSystemInDarkTheme()
    DarkThemeConfig.Enabled -> true
    DarkThemeConfig.Disabled -> false
}