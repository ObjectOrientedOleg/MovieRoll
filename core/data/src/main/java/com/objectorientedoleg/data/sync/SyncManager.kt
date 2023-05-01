package com.objectorientedoleg.data.sync

import kotlinx.coroutines.flow.Flow

interface SyncManager {

    val isSyncing: Flow<Boolean>

    fun sync()
}