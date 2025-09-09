package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.models.WordTranslation
import java.sql.Timestamp

@Entity(tableName = "word_translations")
data class WordTranslationEntity(
    @PrimaryKey()
    val id: Int,

    @ColumnInfo(name = "original_word")
    val original: String,

    @ColumnInfo(name = "translated_word")
    val translated: String,

    @ColumnInfo(name = "timestamp_millis")
    val timestamp: Timestamp,

    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean = false,

    @ColumnInfo(name = "is_in_history")
    val isInHistory: Boolean = true
)

fun WordTranslationEntity.toModel(): WordTranslation {
    return WordTranslation(
        id = this.id,
        original = this.original,
        translated = this.translated,
        timestamp = this.timestamp,
        isFavourite = this.isFavourite,
        isInHistory = this.isInHistory
    )
}

fun WordTranslation.toEntity(): WordTranslationEntity {
    return WordTranslationEntity(
        id = this.id,
        original = this.original,
        translated = this.translated,
        timestamp = this.timestamp,
        isFavourite = this.isFavourite,
        isInHistory = this.isInHistory
    )
}
