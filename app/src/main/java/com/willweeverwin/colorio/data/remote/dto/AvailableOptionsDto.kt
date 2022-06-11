package com.willweeverwin.colorio.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AvailableOptionsDto(
    @SerializedName("result")
    val models: List<String>
)
