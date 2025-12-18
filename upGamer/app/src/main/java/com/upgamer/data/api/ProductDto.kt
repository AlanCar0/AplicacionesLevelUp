package com.upgamer.data.api
import com.google.gson.annotations.SerializedName

data class ProductDto(
    val id: Long,
    val name: String,
    val price: Double,
    @SerializedName("image")
    val imageUrl: String?,
    val stock: Int
)