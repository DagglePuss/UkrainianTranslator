package com.ukrainiantranslator.ui.screens.history

import com.ukrainiantranslator.data.local.TranslationEntity

data class HistoryUiState(
    val translations: List<TranslationEntity> = emptyList(),
    val isLoading: Boolean = true
)
