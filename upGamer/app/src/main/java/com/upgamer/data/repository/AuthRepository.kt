package com.upgamer.data.repository

import com.upgamer.data.api.LoginRequest
import com.upgamer.data.api.LoginResponse
import com.upgamer.data.api.RegisterRequest
import com.upgamer.data.api.RetrofitClient
import com.upgamer.utils.JwtUtils

data class LoginResponseResult(
    val token: String,
    val userId: Long,
    val role: String
)

class AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponseResult> {
        return try {
            val response = RetrofitClient.api.login(
                LoginRequest(email, password)
            )

            if (response.isSuccessful) {
                val body: LoginResponse? = response.body()

                if (body != null) {
                    val roleFromJwt = JwtUtils.getRoleFromToken(body.token) ?: "ROLE_USER"

                    Result.success(
                        LoginResponseResult(
                            token = body.token,
                            userId = body.userId,
                            role = roleFromJwt
                        )
                    )
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        nombre: String,
        rut: String,
        email: String,
        password: String,
        phone: String
    ): Result<Unit> {
        return try {
            val response = RetrofitClient.api.register(
                RegisterRequest(
                    nombre = nombre,
                    rut = rut,
                    email = email,
                    password = password,
                    phone = phone
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al registrar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}