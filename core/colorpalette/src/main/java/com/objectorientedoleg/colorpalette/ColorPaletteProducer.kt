package com.objectorientedoleg.colorpalette

interface ColorPaletteProducer {

    suspend fun paletteFromImage(imageUri: String): Result<ColorPalette>
}