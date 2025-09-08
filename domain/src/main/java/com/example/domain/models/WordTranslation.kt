package com.example.domain.models

import java.sql.Timestamp

data class WordTranslation(
    val id: Int,
    val original: String,
    val translated: String,
    val timestamp: Timestamp,
    val isFavourite: Boolean
)