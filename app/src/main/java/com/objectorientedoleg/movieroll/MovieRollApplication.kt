package com.objectorientedoleg.movieroll

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.objectorientedoleg.core.data.sync.SyncManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class MovieRollApplication : Application(),
    Configuration.Provider,
    ImageLoaderFactory {

    @Inject
    lateinit var syncManager: Provider<SyncManager>

    @Inject
    lateinit var configuration: Provider<Configuration>

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    override fun onCreate() {
        super.onCreate()
        syncMovieRollData()
    }

    private fun syncMovieRollData() = syncManager.get().sync()

    override fun getWorkManagerConfiguration(): Configuration = configuration.get()

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}