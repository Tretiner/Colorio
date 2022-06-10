package com.willweeverwin.colorio.ui.util

import com.willweeverwin.colorio.ui.model.RGBColor


sealed class UIEvent {
    data class RestockOptions(val options: List<String>) : UIEvent()
    data class ChangePalette(val colors: List<RGBColor>) : UIEvent()
    data class ShowSnackbar(val message: String) : UIEvent()
}