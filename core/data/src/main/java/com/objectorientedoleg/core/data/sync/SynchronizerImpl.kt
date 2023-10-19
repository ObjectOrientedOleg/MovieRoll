package com.objectorientedoleg.core.data.sync

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toDateTimePeriod
import javax.inject.Inject

private const val SyncExpirationInMinutes = 60

internal class SynchronizerImpl @Inject constructor() : Synchronizer {

    override suspend fun <T> sync(
        syncDate: suspend () -> Instant?,
        onFetchUpdate: suspend () -> Result<T>,
        onRunUpdate: suspend (T) -> Boolean
    ): Boolean {
        if (isDataValid(syncDate())) return true

        val result = onFetchUpdate()
        return if (result.isSuccess) {
            val data = result.getOrThrow()
            onRunUpdate(data)
        } else {
            false
        }
    }

    override fun isDataValid(syncDate: Instant?): Boolean {
        if (syncDate == null) return false

        val timeElapsed = (Clock.System.now() - syncDate).toDateTimePeriod()
        return timeElapsed.minutes <= SyncExpirationInMinutes
    }
}