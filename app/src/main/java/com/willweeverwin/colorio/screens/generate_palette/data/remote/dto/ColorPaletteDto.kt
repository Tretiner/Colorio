package com.willweeverwin.colorio.screens.generate_palette.data.remote.dto

import com.squareup.moshi.Json
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor

data class ColorPaletteDto(
    @field:Json(name = "result")
    val rawColors: List<List<Int>>
) {
    val colors get(): List<RGBColor> = rawColors.map { RGBColorDto(it).toRGBColor() }
}

