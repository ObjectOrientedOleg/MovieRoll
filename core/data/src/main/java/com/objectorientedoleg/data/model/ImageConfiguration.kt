package com.objectorientedoleg.data.model

import com.objectorientedoleg.database.model.ImageConfigurationEntity

data class ImageConfiguration(
    val baseUrl: String,
    val backdropSizes: List<String>,
    val logoSizes: List<String>,
    val posterSizes: List<String>,
    val profileSizes: List<String>,
    val stillSizes: List<String>
)

fun ImageConfigurationEntity.asModel(): ImageConfiguration =
    ImageConfiguration(
        baseUrl,
        backdropSizes,
        logoSizes,
        posterSizes,
        profileSizes,
        stillSizes
    )