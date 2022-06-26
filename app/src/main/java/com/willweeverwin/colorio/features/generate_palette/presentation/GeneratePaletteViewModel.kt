package com.willweeverwin.colorio.features.generate_palette.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.stream.MalformedJsonException
import com.willweeverwin.colorio.core.util.Resource
import com.willweeverwin.colorio.features.generate_palette.data.remote.repository.ColorPaletteRepository
import com.willweeverwin.colorio.features.generate_palette.presentation.model.ColorPalette
import com.willweeverwin.colorio.features.generate_palette.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneratePaletteViewModel @Inject constructor(
    private val rep: ColorPaletteRepository
) : ViewModel() {

    private var _palette = ColorPalette()
    private var _models = listOf("ui", "default")
    private var _checkedModelIndex = 0

    val models: Collection<String> get() = _models
    val checkedModelIndex: Int get() = _checkedModelIndex

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        refreshModels()
    }

    private fun refreshModels() = viewModelScope.launch {
        when (val resp = rep.refreshModels()) {
            is Resource.Success ->
                _models = resp.data!!

            is Resource.Error -> {
                _eventFlow.emit(UIEvent.ShowSnackbar(resp.message ?: "Something wrong"))
            }
        }
    }

    fun refreshColors() = viewModelScope.launch {
        val reqPalette = _palette.toColorPaletteReq()

        when (val resp = rep.refreshColors(reqPalette)) {
            is Resource.Success -> {
                _palette.changeColors(resp.data!!)
                _eventFlow.emit(UIEvent.ChangeColors(_palette.colors))
                _eventFlow.emit(UIEvent.ShowSnackbar("Palette successfully changed!"))
            }

            is Resource.Error -> {
                if (resp.cause is MalformedJsonException) refreshModels()
                _eventFlow.emit(UIEvent.ShowSnackbar(resp.message ?: "Something wrong"))
            }
        }
    }

    fun toggleLock(i: Int): Boolean = _palette.colors[i].toggleLock()

    fun savePalette() {
        Log.d("save palette", "someday...")
    }

    fun copyToClipboard(ctx: Context, text: String) = viewModelScope.launch {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipText = ClipData.newPlainText("hex_color", text)
        clipboard.setPrimaryClip(clipText)

        _eventFlow.emit(UIEvent.ShowSnackbar("$text was copied to clipboard"))
    }

    fun chooseModelAt(index: Int) = viewModelScope.launch {
        _checkedModelIndex = index
        _palette.model = _models[_checkedModelIndex]
        _eventFlow.emit(UIEvent.ChangeModel(_palette.model))
    }
}