package com.objectorientedoleg.movieroll

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class MovieRollApplication : Application(),
    Configuration.Provider,
    ImageLoaderFactory {

    @Inject
    lateinit var configuration: Provider<Configuration>

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    override fun getWorkManagerConfiguration(): Configuration = configuration.get()

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}