package com.willweeverwin.colorio.screens.generate_palette.presentation.util

import com.willweeverwin.colorio.screens.save_palette.data.local.entity.SavedPaletteEntity

sealed class RecyclerEvent {
    data class ItemUpdated(val pos: Int, val palette: SavedPaletteEntity) : RecyclerEvent()
    data class ItemDeleted(val pos: Int) : RecyclerEvent()
    data class ItemsLoaded(val fromIndex: Int, val size: Int) : RecyclerEvent()
    object Refreshed : RecyclerEvent()
}
