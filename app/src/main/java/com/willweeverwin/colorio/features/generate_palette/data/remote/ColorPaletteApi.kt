package com.willweeverwin.colorio.features.generate_palette.data.remote

import com.willweeverwin.colorio.features.generate_palette.data.remote.dto.AvailableOptionsDto
import com.willweeverwin.colorio.features.generate_palette.data.remote.dto.ColorPaletteDto
import com.willweeverwin.colorio.features.generate_palette.data.remote.req.ColorPaletteReq
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface ColorPaletteApi {

    @GET("/list/")
    suspend fun getAvailableModels(): AvailableOptionsDto

    @Headers("Content-Type: application/json")
    @POST("/api/")
    suspend fun getColors(@Body json: ColorPaletteReq): ColorPaletteDto

    companion object {
        const val BASE_URL = "http://colormind.io/"
    }
}