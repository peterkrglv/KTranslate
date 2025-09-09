package com.example.domain.use_cases

import com.example.domain.models.WordTranslation
import com.example.domain.repositories.CacheRepository

class GetHistoryUseCase(private val cacheRepository: CacheRepository) {
    suspend fun execute(): List<WordTranslation> = cacheRepository.getHistory()
}