package com.objectorientedoleg.core.datastore

import com.objectorientedoleg.core.datastore.UserPreferences.DarkThemeConfig
import kotlinx.coroutines.flow.Flow

interface MovieRollPreferencesDataSource {

    val userPreferences: Flow<UserPreferences>

    suspend fun setDarkThemePreference(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
}