package com.willweeverwin.colorio.screens.generate_palette.data.repository

import com.willweeverwin.colorio.core.util.data.Resource
import com.willweeverwin.colorio.screens.generate_palette.data.remote.req.ColorPaletteRequest
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor

interface GeneratePaletteRepository {
    suspend fun loadModels(): Resource<List<String>>

    suspend fun loadColors(reqPalette: ColorPaletteRequest): Resource<List<RGBColor>>
}