package com.example.ktranslate.translate_screen

import com.example.domain.models.WordTranslation

data class TranslateState(
    val query: String = "",
    val currentTranslation: WordTranslation? = null,
    val isTranslating: Boolean = false,
    val errorMessage: String? = null,
    val history: List<WordTranslation> = emptyList(),
    val isFavourite: Boolean = false,
)

sealed class TranslateEvent {
    data class QueryChanged(val query: String) : TranslateEvent()
    data object TranslateClicked : TranslateEvent()
    data object FavouriteClicked : TranslateEvent()
    data class FavouriteItemClicked(val item: WordTranslation) : TranslateEvent()
}