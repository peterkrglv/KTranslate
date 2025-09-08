package com.example.domain.use_cases

import com.example.domain.models.WordTranslation
import com.example.domain.repositories.TranslationRepository

class TranslateUseCase(private val translationRepository: TranslationRepository) {
    suspend fun execute(query: String): WordTranslation? = translationRepository.translate(query)
}