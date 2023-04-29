package com.objectorientedoleg.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMovies(
    @SerialName("dates") val dates: Dates?,
    @SerialName("page") val page: Int,
    @SerialName("results") val movies: List<NetworkMovie>,
    @SerialName("total_results") val totalResults: Int,
    @SerialName("total_pages") val totalPages: Int
) {
    @Serializable
    data class Dates(
        @SerialName("minimum") val from: String,
        @SerialName("maximum") val to: String
    )
}