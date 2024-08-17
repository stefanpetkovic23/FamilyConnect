package com.example.familyconnectv2.models

import com.google.gson.annotations.SerializedName

data class ToDoGroupRequest(
    @SerializedName("groupId") val groupId: String,
    @SerializedName("taskDetails") val taskDetails: ToDoRequest
)
