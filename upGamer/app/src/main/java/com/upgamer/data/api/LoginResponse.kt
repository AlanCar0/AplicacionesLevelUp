package com.upgamer.data.api

data class LoginResponse(
    val token: String,
    val userId: Long,
    val role: String
)