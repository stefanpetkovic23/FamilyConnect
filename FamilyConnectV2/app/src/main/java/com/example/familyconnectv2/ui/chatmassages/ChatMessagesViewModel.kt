package com.example.familyconnectv2.ui.chatmassages

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.ChatMessageDetails
import com.example.familyconnectv2.models.SendMessageRequest
import com.example.familyconnectv2.models.SendMessageResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatMessagesViewModel : ViewModel() {
    private val chatService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    private val _chatMessages = MutableLiveData<List<ChatMessageDetails>>()
    val chatMessages: LiveData<List<ChatMessageDetails>>
        get() = _chatMessages

    private val _messageSent = MutableLiveData<Boolean>()
    val messageSent: LiveData<Boolean>
        get() = _messageSent

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun getAllMessages(chatId: String) {
        val jwtToken = tokenManager.getToken()

        chatService.getAllMessages("Bearer $jwtToken", chatId)
            .enqueue(object : Callback<List<ChatMessageDetails>> {
                override fun onResponse(
                    call: Call<List<ChatMessageDetails>>,
                    response: Response<List<ChatMessageDetails>>
                ) {
                    if (response.isSuccessful) {
                        _chatMessages.value = response.body()
                        Log.d("ChatMessagesViewModel", "Received chat messages: ${response.body()}")
                        _messageSent.value = true
                    } else {
                        // Handle unsuccessful response
                    }
                }

                override fun onFailure(call: Call<List<ChatMessageDetails>>, t: Throwable) {
                    Log.d("ChatMessagesViewModel", "Received chat messages:  ${t.message}")
                }
            })
    }

    fun sendMessage(request: SendMessageRequest) {
        val jwtToken = tokenManager.getToken()

        chatService.sendMessage("Bearer $jwtToken", request)
            .enqueue(object : Callback<SendMessageResponse> {
                override fun onResponse(
                    call: Call<SendMessageResponse>,
                    response: Response<SendMessageResponse>
                ) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val messageResponse = response.body()
                        Log.d("ChatMessagesViewModel", "Sent message: $messageResponse")
                    } else {
                        // Handle unsuccessful response
                    }
                }

                override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                    // Handle failure
                    Log.d("ChatMessagesViewModel", "Failed to send message: ${t.message}")
                }
            })
    }


}