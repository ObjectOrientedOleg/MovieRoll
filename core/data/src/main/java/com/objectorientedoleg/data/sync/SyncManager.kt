package com.objectorientedoleg.data.sync

interface SyncManager {

    val isSyncing: Boolean

    fun sync()
}