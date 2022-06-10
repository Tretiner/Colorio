package com.willweeverwin.colorio.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.willweeverwin.colorio.ui.model.RGBColor

data class RGBColorDto(
    @SerializedName("")
    val rgb: List<Int>
) {
    fun toRGBColor() = RGBColor(rgb[0], rgb[1], rgb[2])
}