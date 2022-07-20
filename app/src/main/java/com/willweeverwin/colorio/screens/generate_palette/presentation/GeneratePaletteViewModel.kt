package com.willweeverwin.colorio.screens.generate_palette.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.stream.MalformedJsonException
import com.willweeverwin.colorio.R
import com.willweeverwin.colorio.core.util.data.Resource
import com.willweeverwin.colorio.screens.generate_palette.data.repository.GeneratePaletteRepository
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.ColorPalette
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor
import com.willweeverwin.colorio.screens.save_palette.data.local.SavedPalettesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GeneratePaletteViewModel @Inject constructor(
    private val rep: GeneratePaletteRepository,
    private val dao: SavedPalettesDao
) : ViewModel() {

    private var _palette = ColorPalette()
    val paletteColors get() = _palette.colors

    private var _models = listOf("ui", "default")
    val models: Collection<String> get() = _models

    private var _checkedModelIndex = 1
    val checkedModelIndex: Int get() = _checkedModelIndex


    private val _paletteStateFlow = MutableStateFlow<List<RGBColor>?>(_palette.colors)
    val paletteState = _paletteStateFlow.asStateFlow()

    private val _checkedModelStateFlow = MutableStateFlow(_models[_checkedModelIndex])
    val checkedModelState = _checkedModelStateFlow.asStateFlow()

    private val _modelsStateFlow = MutableStateFlow(_models)
    val modelsState = _modelsStateFlow.asStateFlow()

    private val _snackbarFlow = MutableSharedFlow<String>()
    val snackbarState = _snackbarFlow.asSharedFlow()


    init {
        refreshModels()
    }


    private fun refreshModels() = viewModelScope.launch {
        when (val resp = rep.refreshModels()) {
            is Resource.Success -> {
                _models = resp.data!!
                _modelsStateFlow.emit(resp.data)
            }
            is Resource.Error -> _snackbarFlow.emit(resp.message ?: "Undefined error")
        }
    }

    fun refreshColors() = viewModelScope.launch {
        when (val resp = rep.refreshColors(_palette.toPaletteRequest())) {
            is Resource.Success -> {
                _palette.changeColors(resp.data!!)
                // костыль
                _paletteStateFlow.emit(null)
                _paletteStateFlow.emit(_palette.colors)

                _snackbarFlow.emit("Palette successfully changed!")
            }
            is Resource.Error -> {
                // in that case models were probably outdated
                if (resp.cause is MalformedJsonException) refreshModels()

                _snackbarFlow.emit(resp.message ?: "Something wrong")
            }
        }
    }


    suspend fun savePalette(name: String, desc: String) = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        val insertSuccess = dao.insertColorPalette(_palette.toPaletteEntity(name, desc)) != -1L

        if (insertSuccess) _snackbarFlow.emit("Palette was successfully saved!")

        return@withContext insertSuccess
    }

    fun copyToClipboard(ctx: Context, colorString: String) {
        viewModelScope.launch {
            val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipText = ClipData.newPlainText("hex", colorString)
            clipboard.setPrimaryClip(clipText)

            val msgCopySuccess = ctx.resources.getString(R.string.copy_success)
            _snackbarFlow.emit("$colorString $msgCopySuccess")
        }
    }

    fun toggleLock(i: Int): Boolean = _palette.colors[i].toggleLock()

    fun chooseModelAt(index: Int) = viewModelScope.launch {
        _checkedModelIndex = index
        _palette.model = _models[_checkedModelIndex]
        _checkedModelStateFlow.emit(_models[_checkedModelIndex])
    }
}