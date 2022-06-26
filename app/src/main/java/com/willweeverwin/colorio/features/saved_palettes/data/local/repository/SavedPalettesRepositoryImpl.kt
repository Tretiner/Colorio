package com.willweeverwin.colorio.features.saved_palettes.data.local.repository

import com.willweeverwin.colorio.features.saved_palettes.data.local.ColorPaletteDao
import com.willweeverwin.colorio.features.generate_palette.presentation.model.ColorPalette

class SavedPalettesRepositoryImpl(
    private val dao: ColorPaletteDao
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