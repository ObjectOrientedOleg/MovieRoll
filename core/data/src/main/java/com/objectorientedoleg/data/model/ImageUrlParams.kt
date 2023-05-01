package com.objectorientedoleg.data.model

data class ImageUrlParams(
    val path: String,
    val size: Int,
    val type: ImageType
)

enum class ImageType {
    Backdrop,
    Poster,
    Profile
}