package com.example.familyconnectv2.models

import java.util.*

data class ToDoTasksItem(
    val __v: Int,
    val _id: String,
    val completed: Boolean,
    val createdAt: String,
    val createdBy: String,
    val description: String,
    val dueDate: String,
    val title: String,
    val updatedAt: String,
    val group: Group?
)