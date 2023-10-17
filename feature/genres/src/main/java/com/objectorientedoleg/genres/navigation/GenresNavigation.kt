package com.objectorientedoleg.genres.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val GenresGraphRoute = "genres_graph_route"
const val GenresRoute = "genres_route"

fun NavGraphBuilder.homeGraph(
    onMovieClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = GenresGraphRoute,
        startDestination = GenresRoute
    ) {
        nestedGraphs()
    }
}