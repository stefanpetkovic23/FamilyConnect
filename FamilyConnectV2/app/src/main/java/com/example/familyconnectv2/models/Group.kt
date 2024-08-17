package com.example.familyconnectv2.models

data class Group(
    val __v: Int,
    val _id: String,
    val activities: List<Activity>,
    val createdAt: String,
    val name: String,
    val shoppingList: String,
    val tasks: List<Task>,
    val updatedAt: String,
    val users: List<String>
)