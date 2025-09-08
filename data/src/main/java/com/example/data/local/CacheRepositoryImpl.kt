package com.example.data.local

import com.example.domain.models.WordTranslation
import com.example.domain.repositories.CacheRepository

class CacheRepositoryImpl: CacheRepository {
    override suspend fun addHistoryItem(item: WordTranslation) {
        TODO("Not yet implemented")
    }

    override suspend fun updateHistoryItem(item: WordTranslation) {
        TODO("Not yet implemented")
    }

    override suspend fun getHistoryItem(original: String): WordTranslation? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHistoryItem(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun clearHistory() {
        TODO("Not yet implemented")
    }

    override suspend fun getHistory(): List<WordTranslation> {
        TODO("Not yet implemented")
    }

    override suspend fun favouriteTranslation(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromHistory(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}