package com.example.domain.use_cases

import com.example.domain.models.WordTranslation
import jdk.jfr.internal.event.EventConfiguration.timestamp
import java.sql.Timestamp

class SearchUseCase() {
    suspend fun execute(query: String): WordTranslation {
        println("SearchUseCase: query=$query")
        //timestamp is timestamp type
        return WordTranslation(
            id = 1,
            original = query,
            translated = "Translated: $query",
            isFavourite = false,
            timestamp = Timestamp(System.currentTimeMillis())
        )
    }
}