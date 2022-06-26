package com.willweeverwin.colorio.features.generate_palette.di

import com.willweeverwin.colorio.features.generate_palette.data.remote.ColorPaletteApi
import com.willweeverwin.colorio.features.generate_palette.data.remote.ColorPaletteApi.Companion.BASE_URL
import com.willweeverwin.colorio.features.generate_palette.data.remote.repository.ColorPaletteRepository
import com.willweeverwin.colorio.features.generate_palette.data.remote.repository.ColorPaletteRepositoryImpl
import com.willweeverwin.colorio.features.generate_palette.data.remote.util.ClientLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ColorPaletteModule {

    @Provides
    @Singleton
    fun provideColorPaletteRepository(
        api: ColorPaletteApi
    ): ColorPaletteRepository = ColorPaletteRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideColorPaletteApi(): ColorPaletteApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(ClientLogger.create(Level.BODY))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ColorPaletteApi::class.java)
}