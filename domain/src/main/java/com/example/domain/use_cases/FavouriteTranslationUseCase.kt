package com.example.domain.use_cases

class FavouriteTranslationUseCase() {
    suspend fun execute(id: Int, isFavourite: Boolean): Boolean {
        println("FavouriteTranslationUseCase: id=$id, isFavourite=$isFavourite")
        return true
    }
}