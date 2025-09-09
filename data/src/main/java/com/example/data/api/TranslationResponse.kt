package com.example.data.api

data class TranslationResponse(
    val id: Int,
    val text: String,
    val meanings: List<MeaningResponse>
)

data class MeaningResponse(
    val id: Int,
    val translation: TranslationTextResponse
)

data class TranslationTextResponse(
    val text: String
)
