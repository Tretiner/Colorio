package com.willweeverwin.colorio.features.generate_palette.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.willweeverwin.colorio.features.generate_palette.presentation.model.RGBColor

data class ColorPaletteDto(
    @SerializedName("result")
    val rawColors: List<List<Int>>
) {
    val colors get(): List<RGBColor> = rawColors.map { RGBColorDto(it).toRGBColor() }
}

