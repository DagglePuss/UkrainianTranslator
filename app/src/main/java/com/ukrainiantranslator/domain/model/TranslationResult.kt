package com.ukrainiantranslator.domain.model

data class TranslationResult(
    val translatedText: String,
    val detectedLanguageCode: String? = null,
    val detectedConfidence: Double? = null
)
