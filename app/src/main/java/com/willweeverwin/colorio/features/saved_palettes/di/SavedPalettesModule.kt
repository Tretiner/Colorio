package com.willweeverwin.colorio.features.saved_palettes.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.willweeverwin.colorio.features.saved_palettes.data.local.ColorPaletteDatabase
import com.willweeverwin.colorio.features.saved_palettes.data.local.Converters
import com.willweeverwin.colorio.features.saved_palettes.data.util.GsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedPalettesModule {

    @Provides
    @Singleton
    fun provideColorPaletteDatabase(app: Application): ColorPaletteDatabase =
        Room.databaseBuilder(
            app,
            ColorPaletteDatabase::class.java,
            "word_db"
        ).addTypeConverter(Converters(GsonParser(Gson()))).build()
}