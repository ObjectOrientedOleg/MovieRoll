package com.objectorientedoleg.feature.moviedetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.objectorientedoleg.feature.moviedetails.MovieDetailsRoute

internal const val MovieIdArg = "movieId"

fun NavGraphBuilder.movieDetailsScreen(onBackClick: () -> Unit) {
    composable(
        route = "movie_details_route/{$MovieIdArg}",
        arguments = listOf(
            navArgument(MovieIdArg) {
                type = NavType.StringType
            }
        )
    ) {
        MovieDetailsRoute(onBackClick)
    }
}

fun NavController.navigateToMovieDetails(movieId: String) {
    navigate("movie_details_route/$movieId") {
        launchSingleTop = true
    }
}