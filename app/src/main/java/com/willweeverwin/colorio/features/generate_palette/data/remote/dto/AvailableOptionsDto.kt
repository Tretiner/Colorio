package com.willweeverwin.colorio.features.generate_palette.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AvailableOptionsDto(
    @SerializedName("result")
    val models: List<String>
)
