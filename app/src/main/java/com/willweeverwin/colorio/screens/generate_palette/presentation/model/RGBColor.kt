package com.willweeverwin.colorio.screens.generate_palette.presentation.model

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4
import androidx.palette.graphics.Palette
import com.willweeverwin.colorio.R

data class RGBColor(
    var r: Int,
    var g: Int,
    var b: Int,
    var locked: Boolean = false
) {
    val hexString get() = "#%02X%02X%02X".format(r, g, b)

    val resColor get() = Color.rgb(r, g, b)
    
    @Keep
    fun toListOrN(): Any = if (locked) listOf(r, g, b) else "N"

    fun changeColor(newColor: RGBColor) {
        r = newColor.r
        g = newColor.g
        b = newColor.b
    }

    fun toggleLock(): Boolean {
        locked = !locked
        return locked
    }

    @ColorInt
    fun getTextColorRes(context: Context) = ContextCompat.getColor(
        context,
        if (r * 0.299 + g * 0.587 + b * 0.114 > 186)
            R.color.on_surface_dark
        else
            R.color.on_surface_light
    )

    companion object {
        val DEFAULT_COLOR get() = RGBColor(53, 53, 53)

        fun Palette.Swatch.toRGBColor() = rgb.toRGBColor()

        fun Int.toRGBColor() = RGBColor(component2(), component3(), component4())
    }
}