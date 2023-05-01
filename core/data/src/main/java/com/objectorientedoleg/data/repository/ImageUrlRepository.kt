package com.objectorientedoleg.data.repository

import com.objectorientedoleg.data.model.ImageUrlParams
import com.objectorientedoleg.data.sync.Syncable

interface ImageUrlRepository : Syncable {

    suspend fun urlFromParams(imageUrlParams: ImageUrlParams): String?
}