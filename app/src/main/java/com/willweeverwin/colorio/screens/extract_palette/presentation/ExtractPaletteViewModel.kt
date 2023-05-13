package com.willweeverwin.colorio.screens.extract_palette.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.ColorPalette
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor.Companion.toRGBColor
import com.willweeverwin.colorio.screens.save_palette.data.local.SavedPalettesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExtractPaletteViewModel @Inject constructor(
    @ApplicationContext private val appCtx: Context,
    private val dao: SavedPalettesDao
) : ViewModel() {

    private var _img: Bitmap? = null

    private var _palette = ColorPalette()
    val paletteColors get() = _palette.colors

    private val _uiStateFlow = MutableStateFlow(_img to _palette.colors)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _snackbarFlow = MutableSharedFlow<String>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()


    fun extractColors(uri: Uri) = viewModelScope.launch {
        decodeUriToImg(uri)

        if (_img == null) {
            _snackbarFlow.emit("sorry, cant process the image")
            return@launch
        }

        _palette.colors = Palette.from(_img!!)
            .maximumColorCount(14)
            .generate().swatches
            .filterNotNull()
            .take(5)
            .map { it.rgb.toRGBColor() }

        _uiStateFlow.emit(_img to _palette.colors)
        _snackbarFlow.emit("success!")
    }

    private suspend fun decodeUriToImg(uri: Uri) = withContext(Dispatchers.IO) {
        val bitmapOptions = BitmapFactory.Options()

        appCtx.contentResolver.openInputStream(uri).use { stream ->
            _img = BitmapFactory.decodeStream(stream, null, bitmapOptions)
        }
    }

    suspend fun savePalette(name: String, desc: String) = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        val insertSuccess = dao.insertColorPalette(_palette.toPaletteEntity(name, desc)) != -1L

        if (insertSuccess) _snackbarFlow.emit("Palette was successfully saved!")

        return@withContext insertSuccess
    }
}