package com.willweeverwin.colorio.features.generate_palette.presentation.model

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.willweeverwin.colorio.R

data class RGBColor(
    var r: Int,
    var g: Int,
    var b: Int,
    var locked: Boolean = false
) {
    fun changeColor(newColor: RGBColor) {
        r = newColor.r
        g = newColor.g
        b = newColor.b
    }

    fun toggleLock(): Boolean {
        locked = !locked
        return locked
    }

    fun toHexString() = "#%02X%02X%02X".format(r, g, b)

    fun toResColor() = Color.rgb(r, g, b)

    fun toListOrN(): Any = if (locked) listOf(r, g, b) else "N"

    fun getTextColorRes(context: Context) = ContextCompat.getColor(
        context,
        if (r * 0.299 + g * 0.587 + b * 0.114 > 186)
            R.color.on_surface_dark
        else
            R.color.on_surface_light
    )

    companion object {
        val DEFAULT_COLOR get() = RGBColor(53, 53, 53)
    }
}