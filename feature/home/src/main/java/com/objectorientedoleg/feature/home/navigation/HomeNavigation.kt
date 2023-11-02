package com.objectorientedoleg.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.objectorientedoleg.feature.home.HomeRoute

const val HomeGraphRoute = "home_graph_route"
const val HomeRoute = "home_route"

fun NavGraphBuilder.homeGraph(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = HomeGraphRoute,
        startDestination = HomeRoute
    ) {
        composable(route = HomeRoute) {
            HomeRoute(
                onSearchClick = onSearchClick,
                onSettingsClick = onSettingsClick,
                onMovieClick = onMovieClick
            )
        }
        nestedGraphs()
    }
}