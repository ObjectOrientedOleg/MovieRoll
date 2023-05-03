package com.objectorientedoleg.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal const val ConfigurationArg = "HomeScreenConfiguration"

@Parcelize
internal data class HomeScreenConfiguration(val discoverPosterSize: Int) : Parcelable
