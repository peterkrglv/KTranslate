package com.example.ktranslate.favourites_screen

import com.example.domain.models.WordTranslation

data class FavouritesState(
    val favourites: List<WordTranslation> = emptyList(),
    val loadingFavourites: Boolean = true
)

sealed class FavouritesEvent {
    data class UnfavouriteItem(val item: WordTranslation) : FavouritesEvent()
}