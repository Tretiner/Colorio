package com.willweeverwin.colorio.screens.save_palette.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.willweeverwin.colorio.screens.save_palette.data.local.SavedPalettesDatabase
import com.willweeverwin.colorio.screens.save_palette.data.local.Converters
import com.willweeverwin.colorio.screens.save_palette.data.local.SavedPalettesDao
import com.willweeverwin.colorio.screens.save_palette.data.util.GsonParser
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
    fun provideColorPaletteDatabase(app: Application): SavedPalettesDao =
        Room.databaseBuilder(
            app,
            SavedPalettesDatabase::class.java,
            "word_db"
        ).addTypeConverter(Converters(GsonParser(Gson()))).build().dao
}