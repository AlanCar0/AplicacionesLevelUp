package com.upgamer.data.cart

import com.upgamer.data.api.ProductDto

data class CartItem(
    val product: ProductDto,
    var quantity: Int = 1
)