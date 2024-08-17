package com.example.familyconnectv2.models

data class SendMessageChat(
    val _id: String,
    val chatName: String,
    val isGroupChat: Boolean,
    val users: List<ChatUser>,
    val groupAdmin: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val latestMessage: String?
)
