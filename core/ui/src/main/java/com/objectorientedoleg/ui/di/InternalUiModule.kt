package com.objectorientedoleg.ui.di

import com.objectorientedoleg.ui.palette.ColorPaletteProducer
import com.objectorientedoleg.ui.palette.ColorPaletteProducerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface InternalUiModule {

    @Binds
    fun bindsColorPaletteProducer(
        impl: ColorPaletteProducerImpl
    ): ColorPaletteProducer
}