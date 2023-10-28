package com.objectorientedoleg.core.datastore.di

import com.objectorientedoleg.core.datastore.MovieRollPreferencesDataSource
import com.objectorientedoleg.core.datastore.MovieRollPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface InternalDataStoreModule {

    @Binds
    @Singleton
    fun bindsMovieRollPreferencesDataSource(
        impl: MovieRollPreferencesDataSourceImpl
    ): MovieRollPreferencesDataSource
}