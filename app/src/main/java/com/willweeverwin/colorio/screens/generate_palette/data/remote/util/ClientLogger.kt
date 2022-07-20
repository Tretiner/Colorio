package com.willweeverwin.colorio.screens.generate_palette.data.remote.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ClientLogger {
    companion object{
        fun create(logLevel: HttpLoggingInterceptor.Level): OkHttpClient {

            val logging = HttpLoggingInterceptor().apply { level = logLevel }

            return OkHttpClient().newBuilder().apply { addInterceptor(logging) }.build()
        }
    }
}