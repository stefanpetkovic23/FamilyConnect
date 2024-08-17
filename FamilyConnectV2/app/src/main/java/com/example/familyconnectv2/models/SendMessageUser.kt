package com.example.familyconnectv2.models

import com.google.gson.annotations.SerializedName

data class SendMessageUser(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    val picture: String? = null
)
