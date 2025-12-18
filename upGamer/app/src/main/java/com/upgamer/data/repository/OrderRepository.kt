package com.upgamer.data.repository

import com.upgamer.data.api.OrderItemRequest
import com.upgamer.data.api.OrderRequest
import com.upgamer.data.api.RetrofitClient
import com.upgamer.data.cart.CartManager
import com.upgamer.data.session.SessionManager
import com.upgamer.data.api.OrderResponse
import retrofit2.Response
class OrderRepository(
    private val sessionManager: SessionManager
) {

    suspend fun createOrder(): Result<Unit> {
        return try {

            val userId = sessionManager.getUserId()
                ?: return Result.failure(
                    Exception("Usuario no autenticado")
                )

            val items = CartManager.items.map {
                OrderItemRequest(
                    productId = it.product.id,
                    productName = it.product.name,
                    quantity = it.quantity,
                    price = it.product.price
                )
            }

            val request = OrderRequest(
                userId = userId,
                total = CartManager.total(),
                items = items
            )

            val token = sessionManager.getToken()
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val response = RetrofitClient.api.createOrder(
                token = "Bearer $token",
                orderRequest = request
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception("Error al crear la orden (${response.code()})")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getMyOrders(): Result<List<OrderResponse>> {
        return try {
            val token = sessionManager.getToken()
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val response = RetrofitClient.api.getMyOrders(
                token = "Bearer $token"
            )

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("Error al obtener órdenes (${response.code()})")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getOrderDetail(orderId: Long): Result<OrderResponse> {
        return try {
            val response = RetrofitClient.api.getOrderById(orderId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudo obtener el detalle de la orden"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}