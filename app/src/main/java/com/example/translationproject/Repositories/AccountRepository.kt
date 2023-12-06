package com.example.translationproject.Repositories

import com.example.translationproject.UiStates.Credentials
import com.example.translationproject.UiStates.LoginCredentials
import com.example.translationproject.UiStates.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountRepository {
    @POST("/register")
    suspend fun register(@Body credentials: Credentials): Response<ResponseBody>

    @POST("/login")
    suspend fun login(@Body credentials: LoginCredentials): Response<LoginResponse>
}