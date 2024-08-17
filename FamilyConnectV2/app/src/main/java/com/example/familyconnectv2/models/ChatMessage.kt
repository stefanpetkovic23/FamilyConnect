package com.example.familyconnectv2.models

data class ChatMessage(
    val _id: String,
    val chatName: String,
    val isGroupChat: Boolean,
    val users: List<String>,
    val groupAdmin: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val latestMessage: String
)
