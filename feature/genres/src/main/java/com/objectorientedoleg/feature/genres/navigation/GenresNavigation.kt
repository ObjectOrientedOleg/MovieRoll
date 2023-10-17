package com.objectorientedoleg.feature.genres.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.objectorientedoleg.feature.genres.GenresRoute

const val GenresGraphRoute = "genres_graph_route"
const val GenresRoute = "genres_route"

fun NavGraphBuilder.genresGraph(
    onSearchClick: () -> Unit,
    onAccountClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = GenresGraphRoute,
        startDestination = GenresRoute
    ) {
        composable(route = GenresRoute) {
            GenresRoute(
                onSearchClick = onSearchClick,
                onAccountClick = onAccountClick,
                onMovieClick = onMovieClick
            )
        }
        nestedGraphs()
    }
}