package com.willweeverwin.colorio.features.saved_palettes.data.local

import androidx.room.*
import com.willweeverwin.colorio.features.saved_palettes.data.local.entity.ColorPaletteEntity

@Dao
interface ColorPaletteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertColorPalette(palette: ColorPaletteEntity)

    @Delete
    fun deleteColorPalette(palette: ColorPaletteEntity)

    @Query("SELECT * FROM colorpaletteentity WHERE :id == id")
    fun getColorPaletteById(id: Int): ColorPaletteEntity?
}