package com.example.domain.use_cases

class DeleteHistoryItemUseCase {
    suspend fun execute(id: Int): Boolean {
        println("DeleteHistoryItemUseCase: id=$id")
        return true
    }
}