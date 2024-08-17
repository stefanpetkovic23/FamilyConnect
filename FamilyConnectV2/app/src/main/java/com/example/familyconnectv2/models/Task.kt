package com.example.familyconnectv2.models

data class Task(
    val __v: Int,
    val _id: String,
    val completed: Boolean,
    val createdAt: String,
    val createdBy: String, // Dodato polje
    val description: String,
    val dueDate: String,
    val title: String,
    val updatedAt: String
)