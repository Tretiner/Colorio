package com.willweeverwin.colorio.features.saved_palettes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willweeverwin.colorio.features.generate_palette.presentation.model.ColorPalette
import com.willweeverwin.colorio.features.generate_palette.presentation.model.RGBColor

@Entity
data class ColorPaletteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val model: String,
    val colors: List<RGBColor>
) {
    fun toColorPalette() = ColorPalette(
        model = model,
        colors = colors
    )
}