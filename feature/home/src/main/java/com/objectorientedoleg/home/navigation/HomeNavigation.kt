package com.objectorientedoleg.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.objectorientedoleg.home.HomeRoute

const val HomeGraphRoute = "home_graph_route"
const val HomeRoute = "home_route"

fun NavGraphBuilder.homeGraph(
    onMovieClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = HomeGraphRoute,
        startDestination = HomeRoute
    ) {
        composable(route = HomeRoute) {
            HomeRoute(onMovieClick)
        }
        nestedGraphs()
    }
}