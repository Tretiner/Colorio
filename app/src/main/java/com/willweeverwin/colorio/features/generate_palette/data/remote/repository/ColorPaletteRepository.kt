package com.willweeverwin.colorio.features.generate_palette.data.remote.repository

import com.willweeverwin.colorio.core.util.Resource
import com.willweeverwin.colorio.features.generate_palette.data.remote.req.ColorPaletteReq
import com.willweeverwin.colorio.features.generate_palette.presentation.model.RGBColor

interface ColorPaletteRepository {
    suspend fun refreshModels(): Resource<List<String>>

    suspend fun refreshColors(reqPalette: ColorPaletteReq): Resource<List<RGBColor>>
}