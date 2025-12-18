package com.upgamer.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("upgamer_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, userId: Long) {
        prefs.edit()
            .putString("TOKEN", token)
            .putLong("USER_ID", userId)
            .apply()
    }

    fun saveRole(role: String) {
        prefs.edit().putString("ROLE", role).apply()
    }

    fun getRole(): String? =
        prefs.getString("ROLE", null)

    fun getToken(): String? {
        return prefs.getString("TOKEN", null)
    }

    fun getUserId(): Long =
        prefs.getLong("USER_ID", -1L)


    fun clear() {
        prefs.edit().clear().apply()
    }
}