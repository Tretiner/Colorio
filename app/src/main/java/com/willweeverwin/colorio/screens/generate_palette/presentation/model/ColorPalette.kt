package com.willweeverwin.colorio.screens.generate_palette.presentation.model

import com.willweeverwin.colorio.screens.generate_palette.data.remote.req.ColorPaletteReq
import com.willweeverwin.colorio.screens.save_palette.data.local.entity.SavedPaletteEntity

data class ColorPalette(
    var model: String = "default",
    var colors: List<RGBColor> = DEFAULT_PALETTE
) {

    fun toPaletteRequest() = ColorPaletteReq(model, colors.map { it.toListOrN() })

    fun toPaletteEntity(name: String, desc: String) = SavedPaletteEntity(model = model, colors = colors, name = name, desc = desc)

    fun changeColors(newColors: List<RGBColor>) {
        colors.forEachIndexed { i, color -> color.changeColor(newColors[i]) }
    }

    companion object {

        private val DEFAULT_PALETTE = listOf(
            RGBColor.DEFAULT_COLOR,
            RGBColor.DEFAULT_COLOR,
            RGBColor.DEFAULT_COLOR,
            RGBColor.DEFAULT_COLOR,
            RGBColor.DEFAULT_COLOR
        )
    }
}