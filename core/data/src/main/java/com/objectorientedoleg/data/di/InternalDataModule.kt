package com.objectorientedoleg.data.di

import com.objectorientedoleg.data.repository.ImageUrlRepository
import com.objectorientedoleg.data.repository.ImageUrlRepositoryImpl
import com.objectorientedoleg.data.sync.Synchronizer
import com.objectorientedoleg.data.sync.SynchronizerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface InternalDataModule {

    @Binds
    @Singleton
    fun bindsImageUrlRepository(
        impl: ImageUrlRepositoryImpl
    ): ImageUrlRepository

    @Binds
    @Singleton
    fun bindsSynchronizer(
        impl: SynchronizerImpl
    ): Synchronizer
}