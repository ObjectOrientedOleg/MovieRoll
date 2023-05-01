package com.objectorientedoleg.data.sync

import com.objectorientedoleg.data.sync.util.isSyncValid
import kotlinx.datetime.Instant
import javax.inject.Inject

internal interface Synchronizer {

    suspend fun <T> sync(
        syncDate: suspend () -> Instant?,
        onFetchUpdate: suspend () -> Result<T>,
        onRunUpdate: suspend (T) -> Boolean
    ): Boolean
}

internal class SynchronizerImpl @Inject constructor() : Synchronizer {

    override suspend fun <T> sync(
        syncDate: suspend () -> Instant?,
        onFetchUpdate: suspend () -> Result<T>,
        onRunUpdate: suspend (T) -> Boolean
    ): Boolean {
        if (isSyncValid(syncDate())) return true

        val result = onFetchUpdate()
        return if (result.isSuccess) {
            val data = result.getOrThrow()
            onRunUpdate(data)
        } else {
            false
        }
    }
}