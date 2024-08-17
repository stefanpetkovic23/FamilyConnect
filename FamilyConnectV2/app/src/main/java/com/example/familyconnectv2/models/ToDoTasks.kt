package com.example.familyconnectv2.models

data class ToDoTasks(

    val task: ToDoTasksItem,
    val group: Group?,
    val createdBy: String
)