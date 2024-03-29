package com.objectorientedoleg.core.common.scope

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Scope(val scope: MovieRollScope)

enum class MovieRollScope {
    IO
}