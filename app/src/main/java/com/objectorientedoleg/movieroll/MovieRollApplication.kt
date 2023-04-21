package com.objectorientedoleg.movieroll

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory

class MovieRollApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .build()
    }
}