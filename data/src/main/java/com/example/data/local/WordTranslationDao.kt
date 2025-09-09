package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordTranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: WordTranslationEntity): Long

    @Update
    suspend fun updateItem(item: WordTranslationEntity): Int

    @Query("SELECT * FROM word_translations WHERE original_word = :original AND is_in_history = 1 ORDER BY timestamp_millis DESC LIMIT 1")
    suspend fun getHistoryItemByOriginal(original: String): WordTranslationEntity?

    @Query("SELECT * FROM word_translations WHERE id = :id")
    suspend fun getItemById(id: Int): WordTranslationEntity?

    @Query("DELETE FROM word_translations WHERE id = :id")
    suspend fun deleteItemById(id: Int): Int

    @Query("DELETE FROM word_translations WHERE is_in_history = 1")
    suspend fun clearHistory()

    @Query("SELECT * FROM word_translations WHERE is_in_history = 1 ORDER BY timestamp_millis DESC")
    suspend fun getHistoryItems(): List<WordTranslationEntity>

    @Query("UPDATE word_translations SET is_favourite = :isFavourite WHERE id = :id")
    suspend fun setFavouriteStatus(id: Int, isFavourite: Boolean): Int

    @Query("SELECT * FROM word_translations WHERE is_favourite = 1 ORDER BY timestamp_millis DESC")
    suspend fun getFavouriteItems(): List<WordTranslationEntity>
}