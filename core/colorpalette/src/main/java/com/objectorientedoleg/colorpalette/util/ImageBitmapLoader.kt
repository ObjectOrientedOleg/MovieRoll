package com.objectorientedoleg.colorpalette.util

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.executeBlocking
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.objectorientedoleg.common.dispatchers.Dispatcher
import com.objectorientedoleg.common.dispatchers.MovieRollDispatchers.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ImageBitmapLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageLoader: ImageLoader,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun bitmapFromImage(imageUri: String): Result<Bitmap> =
        withContext(ioDispatcher) {
            val request = ImageRequest.Builder(context)
                .data(imageUri)
                .allowHardware(false)
                .build()
            when (val result = imageLoader.executeBlocking(request)) {
                is SuccessResult -> {
                    val bitmap = result.drawable.toBitmap()
                    Result.success(bitmap)
                }

                is ErrorResult -> Result.failure(result.throwable)
            }
        }
}