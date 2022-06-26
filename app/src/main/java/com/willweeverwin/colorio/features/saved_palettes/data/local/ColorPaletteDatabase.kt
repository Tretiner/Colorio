package com.willweeverwin.colorio.features.saved_palettes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.willweeverwin.colorio.features.saved_palettes.data.local.entity.ColorPaletteEntity

@Database(
    version = 1,
    entities = [ColorPaletteEntity::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ColorPaletteDatabase : RoomDatabase()  {
    abstract val dao: ColorPaletteDao
}