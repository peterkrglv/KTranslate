package com.example.data.local

import com.example.domain.models.WordTranslation
import com.example.domain.repositories.CacheRepository
import java.sql.Timestamp

class CacheRepositoryTest : CacheRepository {
    override suspend fun addHistoryItem(item: WordTranslation) {
    }

    override suspend fun updateHistoryItem(item: WordTranslation) {

    }

    override suspend fun getHistoryItem(original: String): WordTranslation? {
        return null
    }

    override suspend fun deleteHistoryItem(id: Int) {

    }

    override suspend fun clearHistory() {
    }

    override suspend fun getHistory(): List<WordTranslation> {
        return emptyList()
    }

    override suspend fun favouriteTranslation(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromHistory(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}