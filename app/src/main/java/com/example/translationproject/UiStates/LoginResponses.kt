package com.example.translationproject.UiStates

data class LoginResponse(
    val tokenType: String,
    val accessToken: String,
    val expiresIn: Integer,
    val refreshToken: String,
)