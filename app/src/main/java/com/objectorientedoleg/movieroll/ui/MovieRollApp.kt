package com.objectorientedoleg.movieroll.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.objectorientedoleg.feature.genres.navigation.genresGraph
import com.objectorientedoleg.feature.home.navigation.HomeGraphRoute
import com.objectorientedoleg.feature.home.navigation.homeGraph
import com.objectorientedoleg.feature.moviedetails.navigation.movieDetailsScreen
import com.objectorientedoleg.feature.moviedetails.navigation.navigateToMovie
import com.objectorientedoleg.core.ui.theme.MovieRollTheme

@Composable
fun MovieRollApp() {
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

        val navController = rememberNavController()

        Scaffold(
            bottomBar = { MovieRollBottomBar(navController) }
        ) { innerPadding ->
            MovieRollNavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}

@Composable
private fun MovieRollBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        TopLevelDestinations.values().forEach { destination ->
            val selected = currentDestination?.route == destination.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
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
private fun MovieRollNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeGraphRoute
    ) {
        homeGraph(onMovieClick = navController::navigateToMovie) {
            movieDetailsScreen(onBackClick = navController::popBackStack)
        }
        genresGraph(
            onSearchClick = {},
            onAccountClick = {},
            onMovieClick = navController::navigateToMovie
        ) {
            movieDetailsScreen(onBackClick = navController::popBackStack)
        }
    }
}