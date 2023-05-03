package com.objectorientedoleg.colorpalette.di

import com.objectorientedoleg.colorpalette.ColorPaletteProducer
import com.objectorientedoleg.colorpalette.ColorPaletteProducerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface InternalColorPaletteModule {

    @Binds
    fun bindsColorPaletteProducer(
        impl: ColorPaletteProducerImpl
    ): ColorPaletteProducer
}