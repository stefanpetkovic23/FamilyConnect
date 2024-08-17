package com.example.familyconnectv2.sp

import android.content.Context
import android.content.SharedPreferences

class TokenManager(private val sharedPreferences: SharedPreferences) {
    private val tokenKey = "token"

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(tokenKey, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(tokenKey, null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove(tokenKey).apply()
    }
}