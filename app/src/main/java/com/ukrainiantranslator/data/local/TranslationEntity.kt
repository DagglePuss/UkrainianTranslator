package com.ukrainiantranslator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLanguageCode: String,
    val sourceLanguageName: String,
    val targetLanguageCode: String,
    val targetLanguageName: String,
    val timestamp: Long = System.currentTimeMillis()
)
