package com.example.familyconnectv2.models

data class SendMessageResponse(
    val sender: SendMessageUser,
    val content: String,
    val chat: SendMessageChat,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
