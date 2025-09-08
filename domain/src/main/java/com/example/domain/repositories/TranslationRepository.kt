package com.example.domain.repositories

import com.example.domain.models.WordTranslation

interface TranslationRepository {
    suspend fun translate(word: String): WordTranslation?
}