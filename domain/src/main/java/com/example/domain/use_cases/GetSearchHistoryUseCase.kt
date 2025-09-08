package com.example.domain.use_cases

import com.example.domain.models.WordTranslation

class GetSearchHistoryUseCase {
    suspend fun execute(): List<WordTranslation> {
        println("GetSearchHistoryUseCase")
        return listOf(
            WordTranslation(
                id = 2,
                original = "Hello",
                translated = "Hola",
                isFavourite = false,
                timestamp = java.sql.Timestamp(System.currentTimeMillis())
            ),
            WordTranslation(
                id = 3,
                original = "World",
                translated = "Mundo",
                isFavourite = true,
                timestamp = java.sql.Timestamp(System.currentTimeMillis())
            )
        )
    }
}