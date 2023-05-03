package com.objectorientedoleg.ui.palette

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorPalette(
    val backgroundColor: Color?,
    val bodyTextColor: Color?,
    val foregroundColor: Color?,
    val titleTextColor: Color?
)