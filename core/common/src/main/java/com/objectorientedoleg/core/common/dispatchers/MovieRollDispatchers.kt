package com.objectorientedoleg.core.common.dispatchers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: MovieRollDispatchers)

enum class MovieRollDispatchers {
    IO
}