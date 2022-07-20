package com.willweeverwin.colorio.screens.save_palette.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.willweeverwin.colorio.screens.save_palette.data.local.entity.SavedPaletteEntity

@Database(
    version = 1,
    entities = [SavedPaletteEntity::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SavedPalettesDatabase : RoomDatabase()  {
    abstract val dao: SavedPalettesDao
}