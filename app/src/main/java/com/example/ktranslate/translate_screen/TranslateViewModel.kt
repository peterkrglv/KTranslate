package com.example.ktranslate.translate_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.WordTranslation
import com.example.domain.use_cases.DeleteHistoryItemUseCase
import com.example.domain.use_cases.FavouriteTranslationUseCase
import com.example.domain.use_cases.GetHistoryUseCase
import com.example.domain.use_cases.TranslateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslateViewModel(
    private val translateUseCase: TranslateUseCase,
    private val favouriteTranslationUseCase: FavouriteTranslationUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
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
                getHistoryUseCase.execute()
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
                favouriteTranslationUseCase.execute(item)
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
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favouriteTranslationUseCase.execute(currentTranslation)
            }
        }
    }

    private fun translateClicked() {
        val query = _viewState.value.query
        if (query.isBlank()) {
            return
        }
        _viewState.update { it.copy(isTranslating = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val translation = translateUseCase.execute(query)
                _viewState.update { currentState ->
                    translation?.let { validTranslation ->
                        currentState.copy(
                            query = validTranslation.original,
                            currentTranslation = validTranslation,
                            isTranslating = false,
                            translationNotFound = false
                        )
                    } ?: currentState.copy(
                        isTranslating = false,
                        translationNotFound = true
                    )
                }
            }
        }
    }

    private fun queryChanged(query: String) {
        _viewState.update { currentState ->
            val currentTranslation = currentState.currentTranslation
            currentTranslation?.let { currentTranslation ->
                val updatedHistory = listOf(currentTranslation) +
                        currentState.history.filter { it.id != currentTranslation.id }
                currentState.copy(
                    query = query,
                    currentTranslation = null,
                    history = updatedHistory,
                    translationNotFound = false
                )
            } ?: run {
                currentState.copy(
                    query = query,
                    translationNotFound = false
                )
            }
        }
    }


    private fun deleteHistoryItemClicked(item: WordTranslation) {
        _viewState.update { currentState ->
            val updatedHistory = currentState.history.filter { it.id != item.id }
            currentState.copy(history = updatedHistory)
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deletHistoryItemUseCase.execute(item)
            }
        }
    }
}
