package com.example.familyconnectv2.models

import com.google.gson.annotations.SerializedName

data class groupResponse(
    @SerializedName("groups") val groups: List<Group>
)