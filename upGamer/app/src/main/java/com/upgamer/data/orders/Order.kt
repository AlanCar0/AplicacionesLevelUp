package com.upgamer.data.orders

import com.upgamer.data.cart.CartItem
import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val items: List<CartItem>,
    val total: Double,
    val createdAt: Long = System.currentTimeMillis()
)