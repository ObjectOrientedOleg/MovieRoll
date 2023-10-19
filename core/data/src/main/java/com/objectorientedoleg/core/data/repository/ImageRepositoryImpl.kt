package com.objectorientedoleg.core.data.repository

import androidx.annotation.GuardedBy
import com.objectorientedoleg.core.common.scope.MovieRollScope.*
import com.objectorientedoleg.core.common.scope.Scope
import com.objectorientedoleg.core.data.model.Image
import com.objectorientedoleg.core.data.model.asModel
import com.objectorientedoleg.core.data.sync.Synchronizer
import com.objectorientedoleg.core.data.util.ImageUrlAssembler
import com.objectorientedoleg.core.database.dao.ImageConfigurationDao
import com.objectorientedoleg.core.database.model.ImageConfigurationEntity
import com.objectorientedoleg.core.network.MovieRollNetworkDataSource
import com.objectorientedoleg.core.network.model.NetworkImageConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

internal class ImageRepositoryImpl @Inject constructor(
    private val imageConfigurationDao: ImageConfigurationDao,
    private val networkDataSource: MovieRollNetworkDataSource,
    @Scope(IO) scope: CoroutineScope
) : ImageRepository {

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

    override suspend fun getImage(params: ImageParams): Image? = urlAssembler()?.let {
        Image { size -> it.assemble(params, size) }
    }

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