package com.objectorientedoleg.network.di

import com.objectorientedoleg.network.MovieRollNetworkDataSource
import com.objectorientedoleg.network.datasource.MovieRollNetworkDataSourceImpl
import com.objectorientedoleg.network.loader.NetworkBitmapLoader
import com.objectorientedoleg.network.loader.NetworkBitmapLoaderImpl
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

    @Binds
    fun bindsNetworkBitmapLoader(
        impl: NetworkBitmapLoaderImpl
    ): NetworkBitmapLoader
}