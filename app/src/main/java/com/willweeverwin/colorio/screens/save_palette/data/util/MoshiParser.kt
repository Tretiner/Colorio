package com.willweeverwin.colorio.screens.save_palette.data.util

import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class MoshiParser(
    private val moshi: Moshi
): JsonParser {

    override fun <T> fromJson(json: String, type: Type): T? =
        moshi.adapter<T>(type).fromJson(json)

    override fun <T> toJson(obj: T, type: Type): String? =
        moshi.adapter<T>(type).toJson(obj)
}
