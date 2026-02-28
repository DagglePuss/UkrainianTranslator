package com.ukrainiantranslator.ui.screens.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukrainiantranslator.data.local.TranslationEntity
import com.ukrainiantranslator.data.repository.TranslationRepository
import com.ukrainiantranslator.domain.model.Language
import com.ukrainiantranslator.util.Constants
import com.ukrainiantranslator.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val repository: TranslationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState: StateFlow<TranslateUiState> = _uiState.asStateFlow()

    init {
        loadLanguages()
    }

    private fun loadLanguages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLanguages = true) }
            when (val result = repository.getLanguages()) {
                is NetworkResult.Success -> {
                    val languages = result.data
                    val defaultSource = languages.find { it.code == Constants.DEFAULT_SOURCE_LANGUAGE }
                    val defaultTarget = languages.find { it.code == Constants.DEFAULT_TARGET_LANGUAGE }
                    _uiState.update {
                        it.copy(
                            availableLanguages = languages,
                            sourceLanguage = defaultSource ?: languages.firstOrNull(),
                            targetLanguage = defaultTarget ?: languages.getOrNull(1),
                            isLoadingLanguages = false
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingLanguages = false,
                            errorMessage = "Failed to load languages: ${result.message}"
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun onSourceTextChanged(text: String) {
        _uiState.update { it.copy(sourceText = text, errorMessage = null) }
    }

    fun onSourceLanguageSelected(language: Language) {
        _uiState.update { it.copy(sourceLanguage = language) }
    }

    fun onTargetLanguageSelected(language: Language) {
        _uiState.update { it.copy(targetLanguage = language) }
    }

    fun onSwapLanguages() {
        _uiState.update {
            it.copy(
                sourceLanguage = it.targetLanguage,
                targetLanguage = it.sourceLanguage,
                sourceText = it.translatedText,
                translatedText = it.sourceText
            )
        }
    }

    fun translate() {
        val state = _uiState.value
        if (state.sourceText.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter text to translate") }
            return
        }
        val sourceLang = state.sourceLanguage ?: return
        val targetLang = state.targetLanguage ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true, errorMessage = null) }

            when (val result = repository.translate(
                text = state.sourceText,
                sourceLanguage = sourceLang.code,
                targetLanguage = targetLang.code
            )) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            translatedText = result.data.translatedText,
                            isTranslating = false
                        )
                    }
                    // Save to history
                    repository.saveTranslation(
                        TranslationEntity(
                            sourceText = state.sourceText,
                            translatedText = result.data.translatedText,
                            sourceLanguageCode = sourceLang.code,
                            sourceLanguageName = sourceLang.name,
                            targetLanguageCode = targetLang.code,
                            targetLanguageName = targetLang.name
                        )
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isTranslating = false,
                            errorMessage = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
