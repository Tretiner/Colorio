package com.willweeverwin.colorio.screens.generate_palette.data.repository

import com.squareup.moshi.JsonEncodingException
import com.willweeverwin.colorio.core.util.data.Resource
import com.willweeverwin.colorio.screens.generate_palette.data.remote.ColorPaletteApi
import com.willweeverwin.colorio.screens.generate_palette.data.remote.req.ColorPaletteRequest
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor
import retrofit2.HttpException
import java.io.IOException
import java.net.ProtocolException

class GeneratePaletteRepositoryImpl(
    private val api: ColorPaletteApi
) : GeneratePaletteRepository {

    override suspend fun loadModels(): Resource<List<String>> =
        try {
            val newModels = api.getAvailableModels().models
            Resource.Success(data = newModels)

        } catch (ex: HttpException) {
            ex.printStackTrace()
            Resource.Error(
                cause = ex,
                message = "Failed to refresh models"
            )

        } catch (ex: IOException) {
            ex.printStackTrace()
            Resource.Error(
                cause = ex,
                message = "Something is wrong"
            )
        }


    override suspend fun loadColors(reqPalette: ColorPaletteRequest): Resource<List<RGBColor>> =
        try {
            val newColors = api.getColors(reqPalette).colors
            Resource.Success(data = newColors)

        } catch (ex: JsonEncodingException) {
            ex.printStackTrace()
            Resource.Error(
                cause = ex,
                message = "Models seems to be outdated"
            )

        } catch (ex: HttpException) {
            ex.printStackTrace()
            Resource.Error(
                cause = ex,
                message = "Check your internet connection"
            )

        } catch (ex: ProtocolException){
            ex.printStackTrace()
            Resource.Error(
                cause = ex,
                message = "Received empty response"
            )
        } catch (ex: IOException) {
            ex.printStackTrace()
            Resource.Error(
                cause = ex,
                message = "Something is wrong"
            )
        }
}