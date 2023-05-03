package com.objectorientedoleg.data.sync

import kotlinx.datetime.Instant

interface Synchronizer {

    suspend fun <T> sync(
        syncDate: suspend () -> Instant?,
        onFetchUpdate: suspend () -> Result<T>,
        onRunUpdate: suspend (T) -> Boolean
    ): Boolean

    fun isDataValid(syncDate: Instant?): Boolean
}