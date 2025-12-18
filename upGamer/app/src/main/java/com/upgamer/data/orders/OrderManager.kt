package com.upgamer.data.orders

import androidx.compose.runtime.mutableStateListOf
import com.upgamer.data.cart.CartManager

object OrderManager {

    val orders = mutableStateListOf<Order>()

    fun createOrder() {
        if (CartManager.items.isEmpty()) return

        val order = Order(
            items = CartManager.items.toList(),
            total = CartManager.total()
        )

        orders.add(order)
        CartManager.clear()
    }
}