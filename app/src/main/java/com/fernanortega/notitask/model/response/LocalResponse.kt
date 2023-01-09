package com.fernanortega.notitask.model.response

sealed class LocalResponse<T> {
    data class Success<T>(val data: T) : LocalResponse<T>()
    data class NullResponse<T>(val exception: NullPointerException) : LocalResponse<T>()
    data class Error<T>(val error: Exception) : LocalResponse<T>()
}
