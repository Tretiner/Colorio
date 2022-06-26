package com.willweeverwin.colorio.features.generate_palette.presentation.util

import com.willweeverwin.colorio.features.generate_palette.presentation.model.RGBColor


sealed class UIEvent {
    data class ChangeColors(val colors: List<RGBColor>) : UIEvent()
    data class ChangeModel(val model: String) : UIEvent()
    data class ShowSnackbar(val message: String) : UIEvent()
}