package com.objectorientedoleg.data.model

import com.objectorientedoleg.data.type.ImageType

data class ImageUrlParams(
    val path: String,
    val size: Int,
    val type: ImageType
)