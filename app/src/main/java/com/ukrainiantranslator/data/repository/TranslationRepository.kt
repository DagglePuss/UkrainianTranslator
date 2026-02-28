package com.ukrainiantranslator.data.repository

import com.ukrainiantranslator.data.local.TranslationEntity
import com.ukrainiantranslator.domain.model.Language
import com.ukrainiantranslator.domain.model.TranslationResult
import com.ukrainiantranslator.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface TranslationRepository {
    suspend fun translate(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): NetworkResult<TranslationResult>

    suspend fun getLanguages(): NetworkResult<List<Language>>

    fun getTranslationHistory(): Flow<List<TranslationEntity>>

    suspend fun saveTranslation(entity: TranslationEntity)

    suspend fun deleteTranslation(entity: TranslationEntity)

    suspend fun clearHistory()
}
