package com.example.familyconnectv2.models

data class SendMessageRequest(
    val content: String,
    val chatId: String
)
