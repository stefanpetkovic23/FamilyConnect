package com.example.familyconnectv2.models

data class ChatMessageDetails(
    val _id: String,
    val sender: ChatUser,
    val content: String,
    val chat: ChatMessage,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
