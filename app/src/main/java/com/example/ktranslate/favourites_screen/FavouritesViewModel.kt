package com.example.ktranslate.favourites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.WordTranslation
import com.example.domain.use_cases.FavouriteTranslationUseCase
import com.example.domain.use_cases.GetFavouritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val favouriteTranslationUseCase: FavouriteTranslationUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(FavouritesState())
    val viewState: StateFlow<FavouritesState>
        get() = _viewState

    init {
        getFavourites()

    }

    fun obtainEvent(event: FavouritesEvent) {
        when (event) {
            is FavouritesEvent.UnfavouriteItem -> unfavouriteItem(event.item)
        }
    }

    private fun getFavourites() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favourites = getFavouritesUseCase.execute()
                _viewState.update { currentState ->
                    currentState.copy(favourites = favourites, loadingFavourites = false)
                }
            }
        }
    }

    private fun unfavouriteItem(item: WordTranslation) {
        _viewState.update { currentState ->
            val updatedFavourites = currentState.favourites.filter { it.id != item.id }
            currentState.copy(favourites = updatedFavourites)
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favouriteTranslationUseCase.execute(item)
            }
        }
    }
}