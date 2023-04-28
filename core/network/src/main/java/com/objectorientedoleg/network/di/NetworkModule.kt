package com.objectorientedoleg.network.di

import android.content.Context
import coil.ImageLoader
import coil.util.DebugLogger
import com.objectorientedoleg.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesCallFactory(): Call.Factory =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                }
            )
            .build()

    @Provides
    @Singleton
    fun providesJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context,
        callFactory: Call.Factory
    ): ImageLoader =
        ImageLoader.Builder(context)
            .callFactory(callFactory)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
}