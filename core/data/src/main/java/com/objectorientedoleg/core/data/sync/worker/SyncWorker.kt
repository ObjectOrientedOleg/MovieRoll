package com.objectorientedoleg.core.data.sync.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.objectorientedoleg.core.common.dispatchers.Dispatcher
import com.objectorientedoleg.core.common.dispatchers.MovieRollDispatchers.*
import com.objectorientedoleg.core.data.R
import com.objectorientedoleg.core.data.repository.GenreRepository
import com.objectorientedoleg.core.data.repository.ImageRepository
import com.objectorientedoleg.core.data.sync.Synchronizer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val synchronizer: Synchronizer,
    private val genreRepository: GenreRepository,
    private val imageRepository: ImageRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(context, workerParameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        applicationContext.getSyncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val syncedSuccessfully = awaitAll(
            async { genreRepository.sync(synchronizer) },
            async { imageRepository.sync(synchronizer) }
        ).all { it }
        if (syncedSuccessfully) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        fun createWorkRequest(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            return OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        }
    }
}

private const val SyncNotificationId = 0
private const val SyncNotificationChannelID = "SyncNotificationChannel"

private fun Context.getSyncForegroundInfo() =
    ForegroundInfo(
        SyncNotificationId,
        getSyncWorkNotification()
    )

private fun Context.getSyncWorkNotification(): Notification {
    val channel = NotificationChannel(
        SyncNotificationChannelID,
        getString(R.string.sync_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = getString(R.string.sync_notification_channel_description)
    }

    val notificationManager: NotificationManager? =
        getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    notificationManager?.createNotificationChannel(channel)

    // TODO add small icon
    return NotificationCompat.Builder(this, SyncNotificationChannelID)
        .setContentTitle(getString(R.string.sync_notification_title))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}