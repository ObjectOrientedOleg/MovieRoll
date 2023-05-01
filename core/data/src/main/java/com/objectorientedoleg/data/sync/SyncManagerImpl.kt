package com.objectorientedoleg.data.sync

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.objectorientedoleg.data.sync.worker.SyncWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

private const val SyncWorkName = "SyncWorkName"

internal class SyncManagerImpl @Inject constructor(
    private val workManager: WorkManager
) : SyncManager {

    override val isSyncing: Flow<Boolean> =
        workManager.getWorkInfosForUniqueWorkLiveData(SyncWorkName)
            .map(List<WorkInfo>::anyRunning)
            .asFlow()
            .conflate()

    override fun sync() {
        workManager.enqueueUniqueWork(
            SyncWorkName,
            ExistingWorkPolicy.KEEP,
            SyncWorker.createWorkRequest()
        )
    }
}

private val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }