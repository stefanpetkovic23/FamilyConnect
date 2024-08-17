package com.example.familyconnectv2.models

import com.google.gson.annotations.SerializedName

data class ToDoGroupResponse(
    @SerializedName("group") val group: Group,
    @SerializedName("task") val task: ToDoTasksItem
)
