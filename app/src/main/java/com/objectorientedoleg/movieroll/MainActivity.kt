package com.objectorientedoleg.movieroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.objectorientedoleg.core.data.sync.SyncManager
import com.objectorientedoleg.core.ui.theme.MovieRollTheme
import com.objectorientedoleg.movieroll.ui.MovieRollApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var syncManager: SyncManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = isSystemInDarkTheme()

            MovieRollTheme(darkTheme) {
                MovieRollApp(syncManager)
            }
        }
    }
}