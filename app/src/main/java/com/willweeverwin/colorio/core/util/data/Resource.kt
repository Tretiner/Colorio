package com.willweeverwin.colorio.core.util.data

import androidx.annotation.Keep

@Keep
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val cause: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(cause: Exception?, message: String, data: T? = null) : Resource<T>(data, message, cause)
}
