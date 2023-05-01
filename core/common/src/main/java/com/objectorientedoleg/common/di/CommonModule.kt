package com.objectorientedoleg.common.di

import com.objectorientedoleg.common.dispatchers.Dispatcher
import com.objectorientedoleg.common.dispatchers.MovieRollDispatchers
import com.objectorientedoleg.common.scope.MovieRollScope
import com.objectorientedoleg.common.scope.Scope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Dispatcher(MovieRollDispatchers.IO)
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @Scope(MovieRollScope.IO)
    fun providesIoScope(
        @Dispatcher(MovieRollDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)
}