package com.objectorientedoleg.movieroll.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.objectorientedoleg.home.navigation.HomeRoute
import com.objectorientedoleg.movieroll.R

enum class TopLevelDestinations(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val label: Int
) {
    Home(
        route = HomeRoute,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        label = R.string.destination_home_label
    )
}