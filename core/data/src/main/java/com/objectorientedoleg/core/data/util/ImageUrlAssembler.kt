package com.objectorientedoleg.core.data.util

import com.objectorientedoleg.core.data.model.ImageConfiguration
import com.objectorientedoleg.core.data.repository.ImageParams
import com.objectorientedoleg.core.data.type.ImageType
import java.util.NavigableMap
import java.util.TreeMap

internal class ImageUrlAssembler(private val configuration: ImageConfiguration) {

    private val backdropSizes by lazy { configuration.backdropSizes.toSizeMap() }
    private val posterSizes by lazy { configuration.posterSizes.toSizeMap() }
    private val profileSizes by lazy { configuration.profileSizes.toSizeMap() }

    fun assemble(params: ImageParams, size: Int): String? {
        val sizeEntry: Map.Entry<Int, String>? = when (params.type) {
            ImageType.Backdrop -> backdropSizes.floorEntry(size)
            ImageType.Poster -> posterSizes.floorEntry(size)
            ImageType.Profile -> profileSizes.floorEntry(size)
        }
        return if (sizeEntry != null) {
            buildUrl(sizeEntry.value, params.path)
        } else {
            val sizeString = when (params.type) {
                ImageType.Backdrop -> configuration.backdropSizes.lastOrNull()
                ImageType.Poster -> configuration.posterSizes.lastOrNull()
                ImageType.Profile -> configuration.profileSizes.lastOrNull()
            }
            sizeString?.let { buildUrl(it, params.path) }
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