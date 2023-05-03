package com.objectorientedoleg.network.di

import com.objectorientedoleg.network.MovieRollNetworkDataSource
import com.objectorientedoleg.network.MovieRollNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface InternalNetworkModule {

    @Binds
    @Singleton
    fun bindsMovieRollNetworkDataSource(
        impl: MovieRollNetworkDataSourceImpl
    ): MovieRollNetworkDataSource
}