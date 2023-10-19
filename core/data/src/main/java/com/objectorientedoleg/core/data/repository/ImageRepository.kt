package com.objectorientedoleg.core.data.repository

import com.objectorientedoleg.core.data.model.Image
import com.objectorientedoleg.core.data.sync.Syncable
import com.objectorientedoleg.core.data.type.ImageType

interface ImageRepository : Syncable {

    suspend fun getImage(params: ImageParams): Image?
}

data class ImageParams(val path: String, val type: ImageType)