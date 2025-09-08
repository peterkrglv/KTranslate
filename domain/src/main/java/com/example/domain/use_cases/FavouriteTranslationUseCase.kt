package com.example.domain.use_cases

import com.example.domain.models.WordTranslation
import com.example.domain.repositories.CacheRepository

class FavouriteTranslationUseCase(private val cacheRepository: CacheRepository) {
    suspend fun execute(item: WordTranslation) {
        cacheRepository.favouriteTranslation(id = item.id)
    }
}