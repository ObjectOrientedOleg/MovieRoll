package com.objectorientedoleg.core.datastore

import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface MovieRollPreferencesDataSource {

    val userData: Flow<UserData>

    suspend fun setDarkThemePreference(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
}