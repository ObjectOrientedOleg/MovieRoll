package com.objectorientedoleg.movieroll.ui

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.objectorientedoleg.core.data.sync.SyncManager
import com.objectorientedoleg.feature.moviedetails.navigation.navigateToMovieDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

@Stable
class MovieRollAppState(
    val navController: NavHostController,
    syncManager: SyncManager,
    coroutineScope: CoroutineScope
) {

    val isSyncing: StateFlow<Boolean> = syncManager.isSyncing
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val _showBottomBar = MutableStateFlow(true)
    val showBottomBar: StateFlow<Boolean> = _showBottomBar.asStateFlow()

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navController.navigate(destination.route, navOptions)
    }

    fun navigateToMovieDetails(movieId: String) {
        navController.navigateToMovieDetails(movieId)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun onDestinationChanged(route: String) {
        val showBottomBar = TopLevelDestination.values().any { it.route == route }
        _showBottomBar.value = showBottomBar
    }
}

@Composable
fun rememberMovieRollAppState(
    syncManager: SyncManager,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MovieRollAppState {
    val appState = remember(
        navController,
        syncManager,
        coroutineScope
    ) {
        MovieRollAppState(
            navController = navController,
            syncManager = syncManager,
            coroutineScope = coroutineScope
        )
    }
    DisposableEffect(Unit) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            if (route != null) {
                appState.onDestinationChanged(route)
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
    return appState
}