package com.example.familyconnectv2.models

data class Chat(
    val _id: String,
    val chatName: String,
    val isGroupChat: Boolean,
    val users: List<ChatUser>,
    val groupAdmin: ChatUser?,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val latestMessage: Message?
)
