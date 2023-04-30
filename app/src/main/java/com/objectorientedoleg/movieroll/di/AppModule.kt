package com.objectorientedoleg.movieroll.di

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesWorkConfiguration(
        hiltWorkerFactory: HiltWorkerFactory
    ): Configuration =
        Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
}