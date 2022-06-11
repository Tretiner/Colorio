package com.willweeverwin.colorio.data.remote

import com.willweeverwin.colorio.data.remote.dto.AvailableOptionsDto
import com.willweeverwin.colorio.data.remote.dto.ColorPaletteDto
import com.willweeverwin.colorio.data.remote.rdc.ColorPaletteRdc
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface ColorPaletteApi {

    @GET("/list/")
    suspend fun getAvailableModels(): AvailableOptionsDto

    @Headers("Content-Type: application/json")
    @POST("/api/")
    suspend fun getColors(@Body json: ColorPaletteRdc): ColorPaletteDto

    companion object {
        const val BASE_URL = "http://colormind.io/"
    }
}