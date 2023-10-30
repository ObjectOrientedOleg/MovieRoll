package com.objectorientedoleg.core.data.repository

import com.objectorientedoleg.core.datastore.MovieRollPreferencesDataSource
import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.model.UserTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(
    private val dataSource: MovieRollPreferencesDataSource
) : UserDataRepository {

    override val userTheme: Flow<UserTheme> = dataSource.userData
        .map { userData ->
            UserTheme(
                darkThemeConfig = userData.darkThemeConfig,
                useDynamicColor = userData.useDynamicColor
            )
        }

    override suspend fun setDarkThemePreference(darkThemeConfig: DarkThemeConfig) =
        dataSource.setDarkThemePreference(darkThemeConfig)

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) =
        dataSource.setDynamicColorPreference(useDynamicColor)
}