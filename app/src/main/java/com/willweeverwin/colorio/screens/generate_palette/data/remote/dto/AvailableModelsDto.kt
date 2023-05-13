package com.willweeverwin.colorio.screens.generate_palette.data.remote.dto

import com.squareup.moshi.Json

data class AvailableModelsDto(
    @field:Json(name = "result")
    val models: List<String>
)
