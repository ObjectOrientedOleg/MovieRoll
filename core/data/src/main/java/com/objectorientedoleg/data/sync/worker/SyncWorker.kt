package com.objectorientedoleg.data.sync.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.objectorientedoleg.common.dispatchers.Dispatcher
import com.objectorientedoleg.common.dispatchers.MovieRollDispatchers.*
import com.objectorientedoleg.data.R
import com.objectorientedoleg.data.repository.GenreRepository
import com.objectorientedoleg.data.repository.ImageUrlRepository
import com.objectorientedoleg.data.repository.MoviesRepository
import com.objectorientedoleg.data.sync.Synchronizer
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
    private val imageUrlRepository: ImageUrlRepository,
    private val moviesRepository: MoviesRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(context, workerParameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        applicationContext.getSyncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val syncedSuccessfully = awaitAll(
            async { genreRepository.sync(synchronizer) },
            async { imageUrlRepository.sync(synchronizer) },
            async { moviesRepository.sync(synchronizer) }
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
    }

    // TODO add small icon
    return NotificationCompat.Builder(this, SyncNotificationChannelID)
        .setContentTitle(getString(R.string.sync_notification_title))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}