package com.upgamer.data.api

data class OrderRequest(
    val userId: Long,
    val items: List<OrderItemRequest>,
    val total: Double,
    val status: String = "CREATED"
)