package com.example.familyconnectv2.models

import java.util.Date

data class ToDoResponse(
    val __v: Int,
    val _id: String,
    val completed: Boolean,
    val createdAt: String,
    val createdBy: String,
    val description: String,
    val dueDate: Date,
    val title: String,
    val updatedAt: String
)