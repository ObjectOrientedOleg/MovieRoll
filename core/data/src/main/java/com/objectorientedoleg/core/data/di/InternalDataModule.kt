package com.objectorientedoleg.core.data.di

import com.objectorientedoleg.core.data.repository.*
import com.objectorientedoleg.core.data.sync.SyncManager
import com.objectorientedoleg.core.data.sync.SyncManagerImpl
import com.objectorientedoleg.core.data.sync.Synchronizer
import com.objectorientedoleg.core.data.sync.SynchronizerImpl
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
    fun bindsGenreRepository(
        impl: GenreRepositoryImpl
    ): GenreRepository

    @Binds
    @Singleton
    fun bindsImageRepository(
        impl: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    @Singleton
    fun bindsMoviesRepository(
        impl: MoviesRepositoryImpl
    ): MoviesRepository

    @Binds
    @Singleton
    fun bindsMovieDetailsRepository(
        impl: MovieDetailsRepositoryImpl
    ): MovieDetailsRepository

    @Binds
    @Singleton
    fun bindsSyncManager(
        impl: SyncManagerImpl
    ): SyncManager

    @Binds
    @Singleton
    fun bindsSynchronizer(
        impl: SynchronizerImpl
    ): Synchronizer

    @Binds
    @Singleton
    fun bindsUserDataRepository(
        impl: UserDataRepositoryImpl
    ): UserDataRepository
}