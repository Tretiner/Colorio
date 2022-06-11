package com.willweeverwin.colorio.ui.util

import com.willweeverwin.colorio.ui.model.RGBColor


sealed class UIEvent {
    data class ChangePalette(val colors: List<RGBColor>) : UIEvent()
    data class ChangeModel(val model: String) : UIEvent()
    data class ShowSnackbar(val message: String) : UIEvent()
    data class SetupUI(val colors: List<RGBColor>, val model: String): UIEvent()
}