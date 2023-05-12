package com.objectorientedoleg.colorpalette

import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.objectorientedoleg.colorpalette.util.ImageBitmapLoader
import com.objectorientedoleg.common.dispatchers.Dispatcher
import com.objectorientedoleg.common.dispatchers.MovieRollDispatchers.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ColorPaletteProducerImpl @Inject constructor(
    private val bitmapLoader: ImageBitmapLoader,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ColorPaletteProducer {

    override suspend fun paletteFromImage(imageUri: String): Result<ColorPalette> =
        withContext(ioDispatcher) {
            bitmapLoader.bitmapFromImage(imageUri).map { bitmap ->
                val palette = Palette.from(bitmap).generate()
                ColorPalette(
                    backgroundColor = palette.darkVibrantSwatch?.rgb?.let(::Color),
                    bodyTextColor = palette.lightMutedSwatch?.bodyTextColor?.let(::Color),
                    foregroundColor = palette.lightMutedSwatch?.rgb?.let(::Color),
                    titleTextColor = palette.lightMutedSwatch?.titleTextColor?.let(::Color)
                )
            }
        }
}