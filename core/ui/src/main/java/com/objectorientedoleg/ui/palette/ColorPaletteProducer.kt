package com.objectorientedoleg.ui.palette

interface ColorPaletteProducer {

    suspend fun paletteFromImage(imageUrl: String): Result<ColorPalette>
}