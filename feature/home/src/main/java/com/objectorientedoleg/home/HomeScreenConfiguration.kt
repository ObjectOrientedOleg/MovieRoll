package com.objectorientedoleg.home

import android.content.Context
import androidx.annotation.Px
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class HomeScreenConfiguration @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @get:Px
    val discoverPosterSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.home_discover_poster_size)
}
