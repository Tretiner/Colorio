package com.willweeverwin.colorio

import com.squareup.moshi.Moshi
import com.willweeverwin.colorio.screens.save_palette.data.util.JsonParser
import com.willweeverwin.colorio.screens.save_palette.data.util.MoshiParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlobalModule {
    @Provides
    @Singleton
    fun provideJsonParser(): JsonParser = MoshiParser(Moshi.Builder().build())
}