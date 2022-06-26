package com.willweeverwin.colorio.features.saved_palettes.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.willweeverwin.colorio.features.generate_palette.presentation.model.RGBColor
import com.willweeverwin.colorio.features.saved_palettes.data.util.JsonParser

@ProvidedTypeConverter
class Converters(
    private val parser: JsonParser
){

    @TypeConverter
    fun fromRGBColorList(colors: List<RGBColor>): String = parser.toJson(
        colors,
        object: TypeToken<List<RGBColor>>(){}.type
    ) ?: ""

    @TypeConverter
    fun toRGBColorList(json: String): List<RGBColor> = parser.fromJson(
        json,
        object: TypeToken<List<RGBColor>>(){}.type
    ) ?: emptyList()
}