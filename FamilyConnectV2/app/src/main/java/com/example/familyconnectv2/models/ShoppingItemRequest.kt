package com.example.familyconnectv2.models

data class ShoppingItemRequest(
    val groupId: String,
    val itemName: String,
    val quantity: Int,
    val isCompleted: Boolean
)
