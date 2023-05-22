package com.objectorientedoleg.data.repository

import com.objectorientedoleg.data.model.Image
import com.objectorientedoleg.data.sync.Syncable
import com.objectorientedoleg.data.type.ImageType

interface ImageRepository : Syncable {

    suspend fun getImage(params: ImageParams): Image?
}

data class ImageParams(val path: String, val type: ImageType)