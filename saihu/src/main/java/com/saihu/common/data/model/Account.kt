package com.saihu.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val token: Token,
    val user: User,
)

@Serializable
data class Token(
    val token: String,
    val tokenExpires: Long,
    val refreshToken: String,
)

@Serializable
data class User(
    val userId: Int,
    val mobile: String,
    val username: String,
)
