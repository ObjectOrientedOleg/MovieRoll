package com.objectorientedoleg.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.objectorientedoleg.core.model.DarkThemeConfig
import com.objectorientedoleg.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

internal class MovieRollPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) : MovieRollPreferencesDataSource {

    override val userData: Flow<UserData> = dataStore.data
        .map { preferences ->
            UserData(
                darkThemeConfig = preferences.darkThemeConfig.asModel(),
                useDynamicColor = preferences.useDynamicColor
            )
        }

    override suspend fun setDarkThemePreference(darkThemeConfig: DarkThemeConfig) =
        dataStore.runUpdateCatching {
            it.copy { this.darkThemeConfig = darkThemeConfig.asProto() }
        }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) =
        dataStore.runUpdateCatching {
            it.copy { this.useDynamicColor = useDynamicColor }
        }
}

private suspend inline fun DataStore<UserPreferences>.runUpdateCatching(
    crossinline block: (UserPreferences) -> UserPreferences
) {
    try {
        updateData { block(it) }
    } catch (e: IOException) {
        Log.e("MovieRollPreferences", "Failed to update user preferences", e)
    }
}

private fun UserPreferences.DarkThemeConfig.asModel() = when (this) {
    UserPreferences.DarkThemeConfig.ENABLED -> DarkThemeConfig.Enabled
    UserPreferences.DarkThemeConfig.DISABLED -> DarkThemeConfig.Disabled
    else -> DarkThemeConfig.FollowSystem
}

private fun DarkThemeConfig.asProto() = when (this) {
    DarkThemeConfig.FollowSystem -> UserPreferences.DarkThemeConfig.FOLLOW_SYSTEM
    DarkThemeConfig.Enabled -> UserPreferences.DarkThemeConfig.ENABLED
    DarkThemeConfig.Disabled -> UserPreferences.DarkThemeConfig.DISABLED
}