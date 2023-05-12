package com.objectorientedoleg.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkGenre(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)
