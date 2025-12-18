package com.upgamer.data.api
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Header
import com.upgamer.data.api.OrderRequest
// -------- LOGIN --------
data class LoginRequest(
    val email: String,
    val password: String
)




// -------- REGISTER --------
data class RegisterRequest(
    val nombre: String,
    val rut: String,
    val email: String,
    val password: String,
    val phone: String
)
interface ApiService {

    @POST("/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>

    @GET("/products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("/orders/{id}")
    suspend fun getOrderById(
        @Path("id") orderId: Long
    ): Response<OrderResponse>
    @POST("/orders/checkout")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body orderRequest: OrderRequest
    ): Response<Unit>

    @GET("/orders/my")
    suspend fun getMyOrders(
        @Header("Authorization") token: String
    ): Response<List<OrderResponse>>

    @POST("/products")
    suspend fun createProduct(
        @Body request: CreateProductRequest
    ): Response<Unit>
    @DELETE("/products/{id}")
    suspend fun deleteProduct(
        @Path("id") productId: Long
    ): Response<Unit>
}
