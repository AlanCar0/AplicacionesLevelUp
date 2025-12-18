package com.upgamer.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {

    fun getRoleFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = String(
                Base64.decode(parts[1], Base64.URL_SAFE),
                Charsets.UTF_8
            )

            val json = JSONObject(payload)
            json.optString("role", null)
        } catch (e: Exception) {
            null
        }
    }
}