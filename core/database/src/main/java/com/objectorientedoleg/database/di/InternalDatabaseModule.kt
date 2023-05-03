package com.objectorientedoleg.database.di

import com.objectorientedoleg.database.proxy.MovieRollDatabaseProxy
import com.objectorientedoleg.database.proxy.MovieRollDatabaseProxyImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface InternalDatabaseModule {

    @Binds
    fun bindsMovieRollDatabaseProxy(
        impl: MovieRollDatabaseProxyImpl
    ): MovieRollDatabaseProxy
}