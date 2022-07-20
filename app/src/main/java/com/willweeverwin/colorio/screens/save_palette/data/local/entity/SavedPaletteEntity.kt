package com.willweeverwin.colorio.screens.save_palette.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor

@Entity(indices = [Index("name", unique = true)])
data class SavedPaletteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "model_name")
    val model: String,

    val colors: List<RGBColor>,

    var name: String,

    var desc: String
)