package com.example.translationproject.UiStates

data class LoginCredentials(val email: String, val password: String, val twoFactorCode: String = "", val twoFactorRecoveryCode: String = "")
data class Credentials(val email: String, val password: String)