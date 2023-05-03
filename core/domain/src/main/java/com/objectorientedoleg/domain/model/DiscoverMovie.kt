package com.objectorientedoleg.domain.model

import com.objectorientedoleg.colorpalette.ColorPalette

data class DiscoverMovie(
    val colorPalette: ColorPalette?,
    val id: Int,
    val posterUrl: String?,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double
)