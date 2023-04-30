package com.objectorientedoleg.data.sync

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toDateTimePeriod

private const val SyncExpirationInMinutes = 60

fun isSyncValid(syncDate: Instant?): Boolean {
    if (syncDate == null) return false

    val timeElapsed = (Clock.System.now() - syncDate).toDateTimePeriod()
    return timeElapsed.minutes <= SyncExpirationInMinutes
}