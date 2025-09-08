package com.example.domain.use_cases

import com.example.domain.models.WordTranslation
import com.example.domain.repositories.CacheRepository

class DeleteHistoryItemUseCase(private val cacheRepository: CacheRepository) {
    suspend fun execute(item: WordTranslation) {
        cacheRepository.deleteHistoryItem(id = item.id)
    }
}