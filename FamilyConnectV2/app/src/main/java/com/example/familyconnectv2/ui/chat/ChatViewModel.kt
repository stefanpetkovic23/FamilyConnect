package com.example.familyconnectv2.ui.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.Chat
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel : ViewModel() {
    private val chatService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>>
        get() = _chats

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun fetchChats() {
        val jwtToken = tokenManager.getToken()

        chatService.fetchChats("Bearer $jwtToken")
            .enqueue(object : Callback<List<Chat>> {
                override fun onResponse(call: Call<List<Chat>>, response: Response<List<Chat>>) {
                    if (response.isSuccessful) {
                        _chats.value = response.body()
                        Log.d("ChatViewModel", "Received chats: ${response.body()}")
                    } else {
                        // Handle unsuccessful response
                    }
                }

                override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                    // Handle network error or request processing failure
                }
            })
    }
}