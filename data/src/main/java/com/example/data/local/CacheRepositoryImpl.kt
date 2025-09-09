package com.example.data.local

import android.util.Log
import com.example.domain.models.WordTranslation
import com.example.domain.repositories.CacheRepository

class CacheRepositoryImpl(private val dao: WordTranslationDao) : CacheRepository {

    override suspend fun addTranslation(item: WordTranslation): Boolean {
        val entity = item.copy(isInHistory = true).toEntity()
        return try {
            dao.insertItem(entity)
            true
        } catch (e: Exception) {
            Log.e("CacheRepo", "Error adding history item", e)
            false
        }
    }

    override suspend fun updateTranslation(item: WordTranslation): Boolean {
        val updatedRows = dao.updateItem(item.copy(isInHistory = true).toEntity())
        return updatedRows > 0
    }

    override suspend fun deleteTranslation(id: Int): Boolean {
        return dao.deleteItemById(id) != 0
    }

    override suspend fun getTranslation(original: String): WordTranslation? {
        return dao.getHistoryItemByOriginal(original)?.toModel()
    }

    override suspend fun clearHistory(): Boolean {
        return try {
            dao.clearHistory()
            true
        } catch (e: Exception) {
            Log.e("CacheRepo", "Error clearing history", e)
            false
        }
    }

    override suspend fun getHistory(): List<WordTranslation> {
        val res = dao.getHistoryItems().map { it.toModel() }
        Log.d("CacheRepo", "Fetched history: $res")
        return res
    }

    override suspend fun deleteFromHistory(id: Int): Boolean {
        val itemEntity = dao.getItemById(id)
        itemEntity?.let {
            val updatedRows = dao.updateItem(it.copy(isInHistory = false))
            return updatedRows > 0
        }
        return false
    }

    override suspend fun favouriteTranslation(item: WordTranslation): Boolean {
        val existingItemEntity = dao.getItemById(item.id)
        if (existingItemEntity == null) {
            return false
        }
        val newIsFavouriteStatus = item.isFavourite
        if (!newIsFavouriteStatus && !existingItemEntity.isInHistory) {
            return deleteTranslation(item.id)
        } else {
            val updatedRows = dao.updateItem(item.toEntity())
            return updatedRows > 0
        }
    }


    override suspend fun getFavourites(): List<WordTranslation> {
        val res = dao.getFavouriteItems().map { it.toModel() }
        Log.d("CacheRepo", "Fetched favourites: $res")
        return res
    }
}
