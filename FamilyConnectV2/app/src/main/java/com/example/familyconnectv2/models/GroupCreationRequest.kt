package com.example.familyconnectv2.models

data class GroupCreationRequest(
    val name: String,
    val users: List<String>)
