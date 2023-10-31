package com.objectorientedoleg.movieroll

import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.model.UserTheme

sealed interface MainActivityUiState {

    data object Loading : MainActivityUiState

    data class Loaded(val userTheme: UserTheme) : MainActivityUiState
}

val MainActivityUiState.config: DarkThemeConfig
    get() = when (this) {
        is MainActivityUiState.Loading -> DarkThemeConfig.FollowSystem
        is MainActivityUiState.Loaded -> userTheme.darkThemeConfig
    }

val MainActivityUiState.dynamicColor: Boolean
    get() = when (this) {
        is MainActivityUiState.Loading -> false
        is MainActivityUiState.Loaded -> userTheme.useDynamicColor
    }