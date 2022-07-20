package com.willweeverwin.colorio.screens.save_palette.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willweeverwin.colorio.screens.generate_palette.presentation.util.RecyclerEvent
import com.willweeverwin.colorio.screens.save_palette.data.local.SavedPalettesDao
import com.willweeverwin.colorio.screens.save_palette.data.local.entity.SavedPaletteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedPalettesViewModel @Inject constructor(
    private val dao: SavedPalettesDao
) : ViewModel() {

    private var _palettes = mutableListOf<SavedPaletteEntity>()
    val palettes: List<SavedPaletteEntity> get() = _palettes

    private val _recyclerFlow = MutableSharedFlow<RecyclerEvent>()
    val recyclerFlow = _recyclerFlow.asSharedFlow()

    private val _snackbarFlow = MutableSharedFlow<String>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()


    init {
        refreshPalettes()
    }


    suspend fun updatePaletteAt(pos: Int, palette: SavedPaletteEntity): Boolean = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        val updated = dao.updateColorPalette(palette) != -1

        if (updated) {
            _palettes[pos] = palette

            _recyclerFlow.emit(RecyclerEvent.ItemUpdated(pos, palette))
            _snackbarFlow.emit("item was successfully saved!")
        }

        updated
    }

    fun deletePaletteAt(pos: Int) = viewModelScope.launch(Dispatchers.IO) {
        dao.deleteColorPalette(_palettes.removeAt(pos))

        _recyclerFlow.emit(RecyclerEvent.ItemDeleted(pos))
        _snackbarFlow.emit("item was deleted!")
    }


    private var currentMinId = 0
    fun refreshPalettes() = viewModelScope.launch(Dispatchers.IO) {
        currentMinId = 999999
        _palettes.clear()

        _recyclerFlow.emit(RecyclerEvent.Refreshed)

        loadNewPalettes()

        _snackbarFlow.emit("refreshed!")
    }

    fun loadNewPalettes() = viewModelScope.launch(Dispatchers.IO) {
        if (currentMinId == 0) return@launch

        val newPalettes = dao.getPalettesWithIdSmallerThan(id = currentMinId, limit = 10)
        if (newPalettes.isEmpty()) return@launch

        val oldSize = _palettes.size
        val size = newPalettes.size

        _palettes.addAll(newPalettes)

        currentMinId = newPalettes.last().id

        _recyclerFlow.emit(RecyclerEvent.ItemsLoaded(size - oldSize, size))
    }
}