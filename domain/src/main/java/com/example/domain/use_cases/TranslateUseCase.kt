package com.example.domain.use_cases

import com.example.domain.models.WordTranslation
import com.example.domain.repositories.CacheRepository
import com.example.domain.repositories.TranslationRepository

class TranslateUseCase(
    private val translationRepository: TranslationRepository,
    private val cacheRepository: CacheRepository
) {
    suspend fun execute(query: String): WordTranslation? {
        val cachedTranslation = cacheRepository.getTranslation(query.lowercase())
        if (cachedTranslation != null) {
            return cachedTranslation
        }
        val tranlation = translationRepository.translate(query)
        tranlation?.let {
            cacheRepository.addTranslation(it)
        }
        return tranlation
    }
}