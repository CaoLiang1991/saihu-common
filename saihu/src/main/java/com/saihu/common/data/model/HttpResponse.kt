package com.saihu.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HttpResponse<T>(
    val msg: String? = "",
    val data: T
)

sealed class ApiState<T> {
    object NotStarted : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val errMsg: String) : ApiState<Nothing>()
}