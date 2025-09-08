package com.example.ktranslate.translate_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.WordTranslation
import com.example.domain.use_cases.DeleteHistoryItemUseCase
import com.example.domain.use_cases.FavouriteTranslationUseCase
import com.example.domain.use_cases.GetSearchHistoryUseCase
import com.example.domain.use_cases.SearchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslateViewModel(
    private val searchUseCase: SearchUseCase,
    private val favouriteTranslationUseCase: FavouriteTranslationUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val deletHistoryItemUseCase: DeleteHistoryItemUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<TranslateState>(TranslateState())
    val viewState: StateFlow<TranslateState>
        get() = _viewState

    init {
        getSearchHistory()
    }

    fun obtainEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.QueryChanged -> queryChanged(event.query)
            is TranslateEvent.TranslateClicked -> translateClicked()
            is TranslateEvent.FavouriteClicked -> favouriteClicked()
            is TranslateEvent.FavouriteHistoryItemClicked -> favouriteSearchItemClicked(event.item)
            is TranslateEvent.DeleteHistoryItemClicked -> deleteHistoryItemClicked(event.item)
        }
    }

    private fun getSearchHistory() {
        viewModelScope.launch {
            val history = withContext(Dispatchers.IO) {
                getSearchHistoryUseCase.execute()
            }
            _viewState.update { currentState ->
                currentState.copy(history = history)
            }
        }
    }

    private fun favouriteSearchItemClicked(item: WordTranslation) {
        val currentState = _viewState.value
        _viewState.update { currentState ->
            val updatedHistory = currentState.history.map {
                if (it.id == item.id) {
                    it.copy(isFavourite = !it.isFavourite)
                } else {
                    it
                }
            }
            currentState.copy(history = updatedHistory)
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favouriteTranslationUseCase.execute(id = item.id, isFavourite = !(item.isFavourite))
            }
        }
    }

    private fun favouriteClicked() {
        val currentTranslation = _viewState.value.currentTranslation
        if (currentTranslation == null) {
            return
        }
        _viewState.update { currentState ->
            val isCurrentlyFavourite = currentTranslation.isFavourite
            val updatedTranslation = currentTranslation.copy(isFavourite = !isCurrentlyFavourite)
            val updatedHistory = currentState.history.map {
                if (it.id == updatedTranslation.id) {
                    updatedTranslation
                } else {
                    it
                }
            }
            currentState.copy(
                currentTranslation = updatedTranslation,
                history = updatedHistory
            )
        }
    }

    private fun translateClicked() {
        val query = _viewState.value.query
        if (query.isBlank()) {
            return
        }
        _viewState.update { it.copy(isTranslating = true) }
        viewModelScope.launch {
            val translation = withContext(Dispatchers.IO) {
                searchUseCase.execute(query)
            }
            _viewState.update { currentState ->
                currentState.copy(
                    currentTranslation = translation,
                    isTranslating = false
                )
            }
        }
    }

    private fun queryChanged(query: String) {
        val currentState = _viewState.value
        val translation = currentState.currentTranslation
        _viewState.value = if (translation == null) {
            currentState.copy(query = query)
        } else {
            val updatedHistory =
                listOf(translation) + currentState.history.filter { it.id != translation.id }
            currentState.copy(query = query, currentTranslation = null, history = updatedHistory)
        }
    }

    private fun deleteHistoryItemClicked(item: WordTranslation) {
        _viewState.update { currentState ->
            val updatedHistory = currentState.history.filter { it.id != item.id }
            currentState.copy(history = updatedHistory)
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deletHistoryItemUseCase.execute(item.id)
            }
        }
    }
}
