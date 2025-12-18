package com.upgamer.data.api

data class OrderItemRequest(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: Double
)