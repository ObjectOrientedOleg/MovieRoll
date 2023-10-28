package com.objectorientedoleg.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.objectorientedoleg.core.datastore.UserPreferences.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

internal class MovieRollPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) : MovieRollPreferencesDataSource {

    override val userPreferences: Flow<UserPreferences> = dataStore.data

    override suspend fun setDarkThemePreference(darkThemeConfig: DarkThemeConfig) =
        dataStore.safeUpdate {
            it.copy { this.darkThemeConfig = darkThemeConfig }
        }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) =
        dataStore.safeUpdate {
            it.copy { this.useDynamicColor = useDynamicColor }
        }
}

private suspend inline fun DataStore<UserPreferences>.safeUpdate(
    crossinline block: (UserPreferences) -> UserPreferences
) {
    try {
        updateData { block(it) }
    } catch (e: IOException) {
        Log.e("MovieRollPreferences", "Failed to update user preferences", e)
    }
}