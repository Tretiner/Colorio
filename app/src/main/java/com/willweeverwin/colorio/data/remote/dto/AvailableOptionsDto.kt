package com.willweeverwin.colorio.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AvailableOptionsDto(
    @SerializedName("models")
    val options: List<String>
)
