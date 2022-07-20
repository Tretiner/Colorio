package com.willweeverwin.colorio.screens.save_palette.data.local

import androidx.room.*
import com.willweeverwin.colorio.screens.save_palette.data.local.entity.SavedPaletteEntity

@Dao
interface SavedPalettesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertColorPalette(palette: SavedPaletteEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateColorPalette(palette: SavedPaletteEntity): Int

    @Delete
    fun deleteColorPalette(palette: SavedPaletteEntity)


    @Query("SELECT * FROM savedpaletteentity WHERE id < :id ORDER BY id DESC LIMIT :limit")
    fun getPalettesWithIdSmallerThan(id: Int, limit: Int): List<SavedPaletteEntity>
}