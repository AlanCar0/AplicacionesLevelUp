package com.upgamer.data.repository

data class LoginApiResponse(
    val token: String,
    val userId: Long,
    val role: String
)