package com.upgamer.data.api

data class CreateProductRequest(
    val name: String,
    val price: Double,
    val stock: Int,
    val image: String? = null,
    val category: String? = null,
    val description: String? = null
)