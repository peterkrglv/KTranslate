package com.example.domain.repositories

import com.example.domain.models.WordTranslation

interface CacheRepository {
    suspend fun addHistoryItem(item: WordTranslation): Unit
    suspend fun updateHistoryItem(item: WordTranslation): Unit
    suspend fun getHistoryItem(original: String): WordTranslation?
    suspend fun deleteHistoryItem(id: Int): Unit
    suspend fun clearHistory(): Unit
    suspend fun getHistory(): List<WordTranslation>

    suspend fun favouriteTranslation(id: Int): Boolean

    suspend fun deleteFromHistory(id: Int): Boolean
}