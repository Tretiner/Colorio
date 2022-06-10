package com.willweeverwin.colorio.ui.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willweeverwin.colorio.data.remote.ColorPaletteApi
import com.willweeverwin.colorio.data.remote.ColorPaletteApi.Companion.BASE_URL
import com.willweeverwin.colorio.ui.model.ColorPalette
import com.willweeverwin.colorio.ui.util.UIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class ColorPaletteViewModel : ViewModel() {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient().newBuilder().apply {
        addInterceptor(logging)
    }.build()

    private val api: ColorPaletteApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(ColorPaletteApi::class.java)

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var palette = ColorPalette()

    fun restockOptions() = viewModelScope.launch {
        try {
            val newOptions = api.getAvailableOptions().options
            _eventFlow.emit(UIEvent.RestockOptions(newOptions))
        } catch (e: HttpException) {
            e.printStackTrace()
            _eventFlow.emit(UIEvent.ShowSnackbar("Check your internet connection"))

        } catch (e: IOException) {
            e.printStackTrace()
            _eventFlow.emit(UIEvent.ShowSnackbar("Something is wrong"))
        }
    }

    fun getPalette() = viewModelScope.launch {
        try {
            val newColors = api.getColors(palette.toColorPalletRdc()).colors

            palette.changeColors(newColors)

            _eventFlow.emit(UIEvent.ChangePalette(palette.colors))
            _eventFlow.emit(UIEvent.ShowSnackbar("Palette successfully changed!"))

        } catch (e: HttpException) {
            e.printStackTrace()
            _eventFlow.emit(UIEvent.ShowSnackbar("Check your internet connection"))

        } catch (e: IOException) {
            e.printStackTrace()
            _eventFlow.emit(UIEvent.ShowSnackbar("Something is wrong"))
        }
    }

    fun changePaletteModel(newModel: String) {
        palette.model = newModel
    }

    fun toggleLock(i: Int): Boolean = palette.colors[i].apply { locked = !locked }.locked

    fun savePalette() {
        Log.d("save palette", "someday...")
    }

    fun copyToClipboard(ctx: Context, text: String) = viewModelScope.launch {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipText = ClipData.newPlainText("hex_color", text)
        clipboard.setPrimaryClip(clipText)

        _eventFlow.emit(UIEvent.ShowSnackbar("$text was copied to clipboard"))
    }

}