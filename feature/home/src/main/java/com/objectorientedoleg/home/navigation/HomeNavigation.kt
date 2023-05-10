package com.objectorientedoleg.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.objectorientedoleg.home.HomeRoute

fun NavGraphBuilder.homeScreen(route: String) {
    composable(route) {
        HomeRoute()
    }
}