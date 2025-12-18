package com.upgamer.data.api

data class OrderResponse(
    val id: Long,
    val total: Double,
    val status: String,
    val createdAt: String
)