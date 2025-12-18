package com.upgamer.data.cart

import androidx.compose.runtime.mutableStateListOf
import com.upgamer.data.api.ProductDto

object CartManager {

    val items = mutableStateListOf<CartItem>()

    fun addProduct(product: ProductDto) {
        val existing = items.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity++
        } else {
            items.add(CartItem(product))
        }
    }

    fun removeProduct(productId: Long) {
        items.removeAll { it.product.id == productId }
    }

    fun clear() {
        items.clear()
    }

    fun total(): Double {
        return items.sumOf { it.product.price * it.quantity }
    }
    fun totalItems(): Int {
        return items.sumOf { it.quantity }
    }
}