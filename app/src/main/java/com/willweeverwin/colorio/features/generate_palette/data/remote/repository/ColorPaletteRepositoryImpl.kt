package com.willweeverwin.colorio.features.generate_palette.data.remote.repository

import com.google.gson.stream.MalformedJsonException
import com.willweeverwin.colorio.core.util.Resource
import com.willweeverwin.colorio.features.generate_palette.data.remote.ColorPaletteApi
import com.willweeverwin.colorio.features.generate_palette.data.remote.req.ColorPaletteReq
import com.willweeverwin.colorio.features.generate_palette.presentation.model.RGBColor
import retrofit2.HttpException
import java.io.IOException

class ColorPaletteRepositoryImpl(
    private val api: ColorPaletteApi
) : ColorPaletteRepository {

    override suspend fun refreshModels(): Resource<List<String>> =
        try {
            val newModels = api.getAvailableModels().models
            Resource.Success(data = newModels)

        } catch (err: HttpException) {
            err.printStackTrace()
            Resource.Error(
                cause = err,
                message = "Failed to refresh models"
            )

        } catch (err: IOException) {
            err.printStackTrace()
            Resource.Error(
                cause = err,
                message = "Something is wrong"
            )
        }


    override suspend fun refreshColors(reqPalette: ColorPaletteReq): Resource<List<RGBColor>> =
        try {
            val newColors = api.getColors(reqPalette).colors
            Resource.Success(data = newColors)

        } catch (err: MalformedJsonException) {
            err.printStackTrace()
            Resource.Error(
                cause = err,
                message = "Models seems to be outdated"
            )

        } catch (err: HttpException) {
            err.printStackTrace()
            Resource.Error(
                cause = err,
                message = "Check your internet connection"
            )

        } catch (err: IOException) {
            err.printStackTrace()
            Resource.Error(
                cause = err,
                message = "Something is wrong"
            )
        }

}