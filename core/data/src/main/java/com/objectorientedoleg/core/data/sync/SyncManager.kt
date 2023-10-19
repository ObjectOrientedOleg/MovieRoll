package com.objectorientedoleg.core.data.sync

import kotlinx.coroutines.flow.Flow

interface SyncManager {

    val isSyncing: Flow<Boolean>

    fun sync()
}