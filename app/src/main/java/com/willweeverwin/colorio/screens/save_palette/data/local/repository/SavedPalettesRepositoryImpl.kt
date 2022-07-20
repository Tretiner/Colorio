package com.willweeverwin.colorio.screens.save_palette.data.local.repository

import com.willweeverwin.colorio.screens.save_palette.data.local.SavedPalettesDao
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.ColorPalette

class SavedPalettesRepositoryImpl(
    private val dao: SavedPalettesDao
): SavedPalettesRepository {

    override suspend fun getColorPaletteById(id: Int): ColorPalette {
        TODO("Not yet implemented")
    }

    override suspend fun getColorPaletteListInIdRange(from: Int, to: Int): List<ColorPalette> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteColorPaletteById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshColorPalettes(palettes: List<ColorPalette>) {
        TODO("Not yet implemented")
    }

}