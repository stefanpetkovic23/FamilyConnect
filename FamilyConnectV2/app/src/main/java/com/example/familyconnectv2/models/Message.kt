package com.example.familyconnectv2.models

data class Message(
    val _id: String,
    val sender: User,
    val content: String,
    val chat: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
