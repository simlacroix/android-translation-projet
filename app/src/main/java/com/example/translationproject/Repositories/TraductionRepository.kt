package com.example.translationproject.Repositories

import com.example.translationproject.UiStates.HistoryItem
import com.example.translationproject.UiStates.Translation
import com.example.translationproject.UiStates.TranslationResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TraductionRepository {
    @POST("/api/Translation")
    suspend fun translate(
        @Body translateInfo: Translation,
        @Header("Authorization") token: String
    ): TranslationResult

    @GET("/api/Translation")
    suspend fun history(@Header("Authorization") token: String): Array<HistoryItem>

    @DELETE("/api/Translation")
    suspend fun clearHistory(@Header("Authorization") token: String)

    @PUT("api/Translation/{id}")
    suspend fun favoriteTranslation(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("isFavorite") isFavorite: Boolean
    )
}