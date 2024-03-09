package com.objectorientedoleg.movieroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.objectorientedoleg.core.data.sync.SyncManager
import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.ui.theme.MovieRollTheme
import com.objectorientedoleg.movieroll.ui.MovieRollApp
import com.objectorientedoleg.movieroll.ui.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var syncManager: SyncManager

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState by mutableStateOf<MainActivityUiState>(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                is MainActivityUiState.Loading -> true
                is MainActivityUiState.Loaded -> false
            }
        }

        enableEdgeToEdge()

        setContent {
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