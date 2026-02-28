package com.ukrainiantranslator.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ukrainiantranslator.data.local.TranslationEntity
import com.ukrainiantranslator.data.repository.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TranslationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getTranslationHistory().collect { translations ->
                _uiState.update {
                    it.copy(
                        translations = translations,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun deleteTranslation(entity: TranslationEntity) {
        viewModelScope.launch {
            repository.deleteTranslation(entity)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
