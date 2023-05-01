package com.objectorientedoleg.data.repository

import androidx.annotation.GuardedBy
import com.objectorientedoleg.data.model.ImageUrlParams
import com.objectorientedoleg.data.model.asModel
import com.objectorientedoleg.data.sync.Synchronizer
import com.objectorientedoleg.data.util.ImageUrlAssembler
import com.objectorientedoleg.database.dao.ImageConfigurationDao
import com.objectorientedoleg.database.model.ImageConfigurationEntity
import com.objectorientedoleg.network.MovieRollNetworkDataSource
import com.objectorientedoleg.network.model.NetworkImageConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

internal class ImageUrlRepositoryImpl @Inject constructor(
    private val imageConfigurationDao: ImageConfigurationDao,
    private val networkDataSource: MovieRollNetworkDataSource,
    scope: CoroutineScope
) : ImageUrlRepository {

    private val mutex = Mutex()

    @GuardedBy("mutex")
    private var urlAssembler: ImageUrlAssembler? = null

    init {
        scope.launch {
            imageConfigurationDao.getImageConfiguration().collect { imageConfigurationEntity ->
                if (imageConfigurationEntity != null) {
                    mutex.withLock {
                        urlAssembler = ImageUrlAssembler(imageConfigurationEntity.asModel())
                    }
                }
            }
        }
    }

    override suspend fun urlFromParams(imageUrlParams: ImageUrlParams): String? =
        urlAssembler()?.urlFromParams(imageUrlParams)

    private suspend fun urlAssembler() = mutex.withLock { urlAssembler }

    override suspend fun sync(synchronizer: Synchronizer): Boolean =
        synchronizer.sync(
            syncDate = { imageConfigurationDao.lastUpdated() },
            onFetchUpdate = { networkDataSource.getImageConfiguration() },
            onRunUpdate = { networkImageConfiguration ->
                imageConfigurationDao.deleteAllAndInsert(networkImageConfiguration.asEntity())
                // Update was successful.
                true
            }
        )
}

private fun NetworkImageConfiguration.asEntity() =
    ImageConfigurationEntity(
        secureBaseUrl,
        backdropSizes,
        logoSizes,
        posterSizes,
        profileSizes,
        stillSizes
    )