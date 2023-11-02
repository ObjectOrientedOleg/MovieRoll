package com.objectorientedoleg.feature.settings

import com.objectorientedoleg.core.model.UserTheme

internal sealed interface SettingsUiState {

    data object Loading : SettingsUiState

    data class Loaded(
        val userTheme: UserTheme,
        val showDynamicColorPreference: Boolean
    ) : SettingsUiState
}