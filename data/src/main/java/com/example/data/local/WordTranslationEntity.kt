package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table") // Указываем имя таблицы
data class WordTranslation(
    @PrimaryKey(autoGenerate = true) // Первичный ключ, генерируется автоматически
    val id: Int = 0, // 0 - значение по умолчанию для Room, чтобы он мог сгенерировать id
    val original: String,         // Оригинальное слово/фраза
    val translated: String,       // Переведенное слово/фраза
    var isFavourite: Boolean = false, // Состояние "в избранном"
    var timestamp: Long             // Время сохранения/обновления (в миллисекундах)
)
