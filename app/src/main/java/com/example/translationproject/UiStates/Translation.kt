package com.example.translationproject.UiStates

data class Translation(
    val sourceLanguage: String,
    val targetLanguages: List<String>,
    val sourceText: String
)

data class TranslationResult(
    val detectedLanguage: DetectedLanguage?,
    val translations: List<TranslationItem>
){
    data class DetectedLanguage(
        val language: String,
        val score: Double
    )

    data class TranslationItem(
        val text: String,
        val to: String
    )
}

data class HistoryItem(
    val id: Int,
    val userId: String,
    val user: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val sourceText: String,
    val translatedText: String,
    val timestamp: String,
    val isFavorite: Boolean
)