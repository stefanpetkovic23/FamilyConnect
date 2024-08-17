package com.example.familyconnectv2.decorators

import com.example.familyconnectv2.models.Chat

interface OnChatClickListener {
    fun onChatClick(chatId: String)
}