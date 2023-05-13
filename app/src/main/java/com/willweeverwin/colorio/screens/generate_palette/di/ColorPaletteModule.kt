package com.willweeverwin.colorio.screens.generate_palette.di

import com.willweeverwin.colorio.screens.generate_palette.data.remote.ColorPaletteApi
import com.willweeverwin.colorio.screens.generate_palette.data.remote.ColorPaletteApi.Companion.BASE_URL
import com.willweeverwin.colorio.screens.generate_palette.data.remote.util.ClientLogger
import com.willweeverwin.colorio.screens.generate_palette.data.repository.GeneratePaletteRepository
import com.willweeverwin.colorio.screens.generate_palette.data.repository.GeneratePaletteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ColorPaletteModule {

    @Provides
    @Singleton
    fun provideColorPaletteRepository(api: ColorPaletteApi): GeneratePaletteRepository =
        GeneratePaletteRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideColorPaletteApi(): ColorPaletteApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(ClientLogger.create(Level.BODY))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ColorPaletteApi::class.java)
}