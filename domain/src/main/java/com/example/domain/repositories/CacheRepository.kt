package com.example.domain.repositories

import com.example.domain.models.WordTranslation

interface CacheRepository {
    suspend fun addTranslation(item: WordTranslation): Boolean
    suspend fun updateTranslation(item: WordTranslation): Boolean

    suspend fun deleteTranslation(id: Int): Boolean
    suspend fun getTranslation(original: String): WordTranslation?
    suspend fun clearHistory(): Boolean
    suspend fun getHistory(): List<WordTranslation>
    suspend fun deleteFromHistory(id: Int): Boolean
    suspend fun favouriteTranslation(item: WordTranslation): Boolean
    suspend fun getFavourites(): List<WordTranslation>
}