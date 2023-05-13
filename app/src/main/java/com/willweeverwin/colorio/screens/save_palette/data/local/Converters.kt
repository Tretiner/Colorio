package com.willweeverwin.colorio.screens.save_palette.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Types
import com.willweeverwin.colorio.screens.generate_palette.presentation.model.RGBColor
import com.willweeverwin.colorio.screens.save_palette.data.util.JsonParser

@ProvidedTypeConverter
class Converters(
    private val parser: JsonParser
) {
    @TypeConverter
    fun fromRGBColorList(colors: List<RGBColor>): String = parser.toJson(
        colors,
        Types.newParameterizedType(
            List::class.java,
            RGBColor::class.java
        )
    ) ?: ""

    @TypeConverter
    fun toRGBColorList(json: String): List<RGBColor> = parser.fromJson(
        json,
        Types.newParameterizedType(
            List::class.java,
            RGBColor::class.java
        )
    ) ?: emptyList()
}