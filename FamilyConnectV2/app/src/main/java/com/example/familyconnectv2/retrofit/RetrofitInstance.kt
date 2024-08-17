package com.example.familyconnectv2.retrofit

import android.content.Context
import com.example.familyconnectv2.interfaces.API
import com.example.familyconnectv2.sp.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: API by lazy {
        Retrofit.Builder().baseUrl("http://10.66.249.249:5000").addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }
}