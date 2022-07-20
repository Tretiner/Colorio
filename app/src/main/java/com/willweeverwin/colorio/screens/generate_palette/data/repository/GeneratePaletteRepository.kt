package com.willweeverwin.colorio.screens.generate_palette.data.repository

import com.willweeverwin.colorio.core.util.data.Resource
import com.willweeverwin.colorio.screens.generate_palette.data.remote.req.ColorPaletteReq
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor

interface GeneratePaletteRepository {
    suspend fun refreshModels(): Resource<List<String>>

    suspend fun refreshColors(reqPalette: ColorPaletteReq): Resource<List<RGBColor>>
}