package com.willweeverwin.colorio.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.willweeverwin.colorio.ui.model.RGBColor

data class ColorPaletteDto(
    @SerializedName("result")
    val rawColors: List<List<Int>>
) {
    val colors get(): List<RGBColor> = rawColors.map { RGBColorDto(it).toRGBColor() }
}

