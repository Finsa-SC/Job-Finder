package com.example.gawe17.Model

data class UserSession(
    val userId: Int,
    val profilePicture: String?,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val role: String
)