package com.ukrainiantranslator.ui.screens.translate

import com.ukrainiantranslator.domain.model.Language

data class TranslateUiState(
    val sourceText: String = "",
    val translatedText: String = "",
    val sourceLanguage: Language? = null,
    val targetLanguage: Language? = null,
    val availableLanguages: List<Language> = emptyList(),
    val isTranslating: Boolean = false,
    val isLoadingLanguages: Boolean = true,
    val errorMessage: String? = null
)
