package com.ukrainiantranslator.data.repository

import com.ukrainiantranslator.data.local.TranslationDao
import com.ukrainiantranslator.data.local.TranslationEntity
import com.ukrainiantranslator.data.remote.LibreTranslateApi
import com.ukrainiantranslator.data.remote.dto.TranslateRequest
import com.ukrainiantranslator.domain.model.Language
import com.ukrainiantranslator.domain.model.TranslationResult
import com.ukrainiantranslator.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationRepositoryImpl @Inject constructor(
    private val api: LibreTranslateApi,
    private val dao: TranslationDao
) : TranslationRepository {

    private var cachedLanguages: List<Language>? = null

    override suspend fun translate(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): NetworkResult<TranslationResult> {
        return try {
            val request = TranslateRequest(
                text = text,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
            val response = api.translate(request)
            NetworkResult.Success(
                TranslationResult(
                    translatedText = response.translatedText,
                    detectedLanguageCode = response.detectedLanguage?.language,
                    detectedConfidence = response.detectedLanguage?.confidence
                )
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.localizedMessage ?: "Translation failed",
                exception = e
            )
        }
    }

    override suspend fun getLanguages(): NetworkResult<List<Language>> {
        cachedLanguages?.let { return NetworkResult.Success(it) }

        return try {
            val dtos = api.getLanguages()
            val languages = dtos.map { dto ->
                Language(
                    code = dto.code,
                    name = dto.name,
                    targets = dto.targets
                )
            }.sortedWith(
                compareBy<Language> { it.code != "uk" }
                    .thenBy { it.name }
            )
            cachedLanguages = languages
            NetworkResult.Success(languages)
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.localizedMessage ?: "Failed to load languages",
                exception = e
            )
        }
    }

    override fun getTranslationHistory(): Flow<List<TranslationEntity>> {
        return dao.getRecentTranslations()
    }

    override suspend fun saveTranslation(entity: TranslationEntity) {
        dao.insertTranslation(entity)
    }

    override suspend fun deleteTranslation(entity: TranslationEntity) {
        dao.deleteTranslation(entity)
    }

    override suspend fun clearHistory() {
        dao.clearAllHistory()
    }
}
