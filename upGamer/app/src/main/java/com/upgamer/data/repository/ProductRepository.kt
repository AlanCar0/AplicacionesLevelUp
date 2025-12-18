package com.upgamer.data.repository

import com.upgamer.data.api.ProductDto
import com.upgamer.data.api.RetrofitClient
import com.upgamer.data.api.CreateProductRequest

class ProductRepository {

    suspend fun getProducts(): Result<List<ProductDto>> {
        return try {
            val response = RetrofitClient.api.getProducts()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun createProduct(
        name: String,
        price: Double,
        stock: Int,
        image: String?,
        category: String?,
        description: String?
    ): Result<Unit> {
        return try {
            val response = RetrofitClient.api.createProduct(
                CreateProductRequest(
                    name = name,
                    price = price,
                    stock = stock,
                    image = image,
                    category = category,
                    description = description
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun deleteProduct(productId: Long): Result<Unit> {
        return try {
            val response = RetrofitClient.api.deleteProduct(productId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
