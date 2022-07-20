package com.willweeverwin.colorio.screens.save_palette.data.local.repository

import com.willweeverwin.colorio.screens.generate_palette.presentation.model.ColorPalette

interface SavedPalettesRepository {
    suspend fun getColorPaletteById(id: Int): ColorPalette

    suspend fun getColorPaletteListInIdRange(from: Int, to: Int): List<ColorPalette>

    suspend fun deleteColorPaletteById(id: Int)

    suspend fun refreshColorPalettes(palettes: List<ColorPalette>)
}