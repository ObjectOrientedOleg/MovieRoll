package com.objectorientedoleg.colorpalette

interface ColorPaletteProducer {

    suspend fun paletteFromImage(imageUrl: String): Result<ColorPalette>
}