package com.objectorientedoleg.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkGenres(@SerialName("genres") val genres: List<NetworkGenre>)
