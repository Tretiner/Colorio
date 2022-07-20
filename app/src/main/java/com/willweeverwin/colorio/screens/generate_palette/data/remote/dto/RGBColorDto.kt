package com.willweeverwin.colorio.screens.generate_palette.data.remote.dto

import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor

data class RGBColorDto(
    val rgb: List<Int>
) {
    fun toRGBColor() = RGBColor(rgb[0], rgb[1], rgb[2])
}