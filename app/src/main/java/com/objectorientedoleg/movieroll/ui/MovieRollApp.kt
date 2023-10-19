package com.objectorientedoleg.movieroll.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.objectorientedoleg.core.data.sync.SyncManager
import com.objectorientedoleg.core.ui.components.MovieRollLoadingIndicator
import com.objectorientedoleg.core.ui.theme.MovieRollTheme
import com.objectorientedoleg.feature.genres.navigation.genresGraph
import com.objectorientedoleg.feature.home.navigation.HomeGraphRoute
import com.objectorientedoleg.feature.home.navigation.homeGraph
import com.objectorientedoleg.feature.moviedetails.navigation.movieDetailsScreen

@Composable
fun MovieRollApp(syncManager: SyncManager) {
    val appState = rememberMovieRollAppState(syncManager)
    val darkTheme = isSystemInDarkTheme()

    MovieRollTheme(darkTheme) {
        val systemUiController = rememberSystemUiController()
        val statusBarColor = MaterialTheme.colorScheme.background
        val navigationBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
            NavigationBarDefaults.Elevation
        )
        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor, !darkTheme)
            systemUiController.setNavigationBarColor(navigationBarColor, !darkTheme)
        }

        val showBottomBar by appState.showBottomBar.collectAsStateWithLifecycle()
        val isSyncing by appState.isSyncing.collectAsStateWithLifecycle()

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    MovieRollBottomBar(
                        navController = appState.navController,
                        onNavItemClick = appState::navigateToTopLevelDestination
                    )
                }
            }
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
        homeGraph(onMovieClick = onNavigateToMovieDetails) {
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