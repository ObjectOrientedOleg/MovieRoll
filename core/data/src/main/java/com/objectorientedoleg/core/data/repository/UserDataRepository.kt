package com.objectorientedoleg.core.data.repository

import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.model.UserTheme
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userTheme: Flow<UserTheme>

    suspend fun setDarkThemePreference(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
}