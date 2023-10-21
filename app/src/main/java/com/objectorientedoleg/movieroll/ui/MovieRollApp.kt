package com.objectorientedoleg.movieroll.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.objectorientedoleg.core.data.sync.SyncManager
import com.objectorientedoleg.core.ui.components.MovieRollGradientBackground
import com.objectorientedoleg.core.ui.components.MovieRollLoadingIndicator
import com.objectorientedoleg.feature.genres.navigation.genresGraph
import com.objectorientedoleg.feature.home.navigation.HomeGraphRoute
import com.objectorientedoleg.feature.home.navigation.homeGraph
import com.objectorientedoleg.feature.moviedetails.navigation.movieDetailsScreen

@Composable
fun MovieRollApp(syncManager: SyncManager) {
    val appState = rememberMovieRollAppState(syncManager)
    val showBottomBar by appState.showBottomBar.collectAsStateWithLifecycle()
    val isSyncing by appState.isSyncing.collectAsStateWithLifecycle()

    MovieRollGradientBackground {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    MovieRollBottomBar(
                        navController = appState.navController,
                        onNavItemClick = appState::navigateToTopLevelDestination
                    )
                }
            },
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0)
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isSyncing) {
                    MovieRollLoadingIndicator(Modifier.align(Alignment.Center))
                } else {
                    MovieRollNavHost(
                        navController = appState.navController,
                        onNavigateBack = appState::navigateBack,
                        onNavigateToMovieDetails = appState::navigateToMovieDetails
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieRollBottomBar(
    navController: NavHostController,
    onNavItemClick: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        TopLevelDestination.values().forEach { destination ->
            val selected = currentDestination?.route == destination.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavItemClick(destination) },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.label)) }
            )
        }
    }
}

@Composable
private fun MovieRollNavHost(
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = HomeGraphRoute
    ) {
        homeGraph(
            onSearchClick = {},
            onAccountClick = {},
            onMovieClick = onNavigateToMovieDetails
        ) {
            movieDetailsScreen(onBackClick = onNavigateBack)
        }
        genresGraph(
            onSearchClick = {},
            onAccountClick = {},
            onMovieClick = onNavigateToMovieDetails
        ) {
            movieDetailsScreen(onBackClick = onNavigateBack)
        }
    }
}