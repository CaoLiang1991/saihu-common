package com.saihu.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HttpResponse<T>(
    val code: Int,
    val msg: String? = "",
    val data: T
)

@Serializable
data class HttpPage<T>(
    val total: Int,
    var totalPage: Int = 1,
    val records: T
)

sealed class ApiState<T> {
    object NotStarted : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val errMsg: String) : ApiState<Nothing>()
}