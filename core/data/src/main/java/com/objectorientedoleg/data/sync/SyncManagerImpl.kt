package com.objectorientedoleg.data.sync

import androidx.work.WorkManager
import javax.inject.Inject

internal class SyncManagerImpl @Inject constructor(
    private val workManager: WorkManager
) : SyncManager {

    override val isSyncing: Boolean
        get() = TODO("Not yet implemented")

    override fun sync() {
        TODO("Not yet implemented")
    }
}