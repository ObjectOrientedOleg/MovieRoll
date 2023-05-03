package com.objectorientedoleg.network.loader

import android.graphics.Bitmap

interface NetworkBitmapLoader {

    suspend fun bitmapFromImage(imageUrl: String): Result<Bitmap>
}