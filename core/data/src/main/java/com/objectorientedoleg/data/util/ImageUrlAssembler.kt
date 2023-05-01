package com.objectorientedoleg.data.util

import com.objectorientedoleg.data.model.ImageConfiguration
import com.objectorientedoleg.data.model.ImageType
import com.objectorientedoleg.data.model.ImageUrlParams
import java.util.NavigableMap
import java.util.TreeMap

internal class ImageUrlAssembler(private val configuration: ImageConfiguration) {

    private val backdropSizes = configuration.backdropSizes.toSizeMap()
    private val posterSizes = configuration.posterSizes.toSizeMap()
    private val profileSizes = configuration.profileSizes.toSizeMap()

    fun urlFromParams(imageUrlParams: ImageUrlParams): String? {
        val sizeEntry: Map.Entry<Int, String>? = when (imageUrlParams.type) {
            ImageType.Backdrop -> backdropSizes.floorEntry(imageUrlParams.size)
            ImageType.Poster -> posterSizes.floorEntry(imageUrlParams.size)
            ImageType.Profile -> profileSizes.floorEntry(imageUrlParams.size)
        }
        return if (sizeEntry != null) {
            buildUrl(sizeEntry.value, imageUrlParams.path)
        } else {
            val sizeString = when (imageUrlParams.type) {
                ImageType.Backdrop -> configuration.backdropSizes.lastOrNull()
                ImageType.Poster -> configuration.posterSizes.lastOrNull()
                ImageType.Profile -> configuration.profileSizes.lastOrNull()
            }
            sizeString?.let { size ->
                buildUrl(size, imageUrlParams.path)
            }
        }
    }

    private fun buildUrl(size: String, path: String): String {
        return buildString {
            append(configuration.baseUrl)
            append(size)
            append(path)
        }
    }

}

private fun List<String>.toSizeMap(): NavigableMap<Int, String> {
    return mapNotNull { sizeString ->
        sizeString
            .replace("\\D".toRegex(), "")
            .toIntOrNull()
            ?.let { size ->
                size to sizeString
            }
    }.toMap(TreeMap())
}