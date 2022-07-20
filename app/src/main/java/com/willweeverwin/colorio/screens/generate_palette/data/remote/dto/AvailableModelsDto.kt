package com.willweeverwin.colorio.screens.generate_palette.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AvailableModelsDto(
    @SerializedName("result")
    val models: List<String>
)
