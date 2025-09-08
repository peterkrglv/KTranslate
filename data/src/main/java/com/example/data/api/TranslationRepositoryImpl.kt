package com.example.data.api

import android.util.Log
import com.example.domain.models.WordTranslation
import com.example.domain.repositories.TranslationRepository
import java.sql.Timestamp

class TranslationRepositoryImpl(
    private val translationApi: TranslationApi
) : TranslationRepository {
    override suspend fun translate(word: String): WordTranslation? {
        try {
            val response = translationApi.translate(word)
            if (response.isNotEmpty()) {
                val meaning = response[0].meanings
                if (meaning.isNotEmpty()) {
                    val translatedWord = meaning[0].translation.text
                    Log.d(
                        "TranslationRepository",
                        "Translated '${response[0].text}' to '$translatedWord'"
                    )
                    return WordTranslation(
                        id = response[0].id,
                        original = response[0].text,
                        translated = translatedWord,
                        timestamp = Timestamp(System.currentTimeMillis()),
                        isFavourite = false,
                        isInHistory = true
                    )
                } else {
                    Log.e("TranslationRepository", "No meanings found for word '$word'")
                    return null
                }
            } else {
                Log.e("TranslationRepository", "Empty response for word '$word'")
                return null
            }
        } catch (e: Exception) {
            Log.e("TranslationRepository", "Error during translation", e)
            return null
        }
    }
}