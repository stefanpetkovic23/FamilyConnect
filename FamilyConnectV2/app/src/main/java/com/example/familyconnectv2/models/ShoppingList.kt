package com.example.familyconnectv2.models

data class ShoppingList(
    val _id: String,
    val name: String,
    val createdBy: String,
    val items: List<ShoppingItem>,
    val __v: Int
)
