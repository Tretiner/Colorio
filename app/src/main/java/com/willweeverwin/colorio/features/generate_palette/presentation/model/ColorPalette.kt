package com.willweeverwin.colorio.features.generate_palette.presentation.model

import com.willweeverwin.colorio.features.generate_palette.data.remote.req.ColorPaletteReq
import com.willweeverwin.colorio.features.saved_palettes.data.local.entity.ColorPaletteEntity

data class ColorPalette(
    var model: String = "default",
    var colors: List<RGBColor> = listOf(
        RGBColor.DEFAULT_COLOR,
        RGBColor.DEFAULT_COLOR,
        RGBColor.DEFAULT_COLOR,
        RGBColor.DEFAULT_COLOR,
        RGBColor.DEFAULT_COLOR,
    )
) {
    fun toColorPaletteReq() = ColorPaletteReq(model, colors.map { it.toListOrN() })

    fun toColorPaletteEntity() = ColorPaletteEntity(model = model, colors = colors)

    fun changeColors(newColors: List<RGBColor>) {
        colors.forEachIndexed { i, color -> color.changeColor(newColors[i]) }
    }
}