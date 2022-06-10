package com.willweeverwin.colorio.ui.model

import com.willweeverwin.colorio.data.remote.rdc.ColorPaletteRdc

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
    fun toColorPalletRdc() = ColorPaletteRdc(model, colors.map { it.toListOrN() })

    fun changeColors(newColors: List<RGBColor>) {
        colors.forEachIndexed { i, color -> color.changeColor(newColors[i]) }
    }
}