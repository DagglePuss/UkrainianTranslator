package com.ukrainiantranslator.ui.screens.translate

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ukrainiantranslator.domain.model.Language
import com.ukrainiantranslator.util.ClipboardHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateScreen(
    viewModel: TranslateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ukrainian Translator",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Language selector row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LanguageDropdown(
                label = "From",
                selectedLanguage = uiState.sourceLanguage,
                languages = uiState.availableLanguages,
                onLanguageSelected = viewModel::onSourceLanguageSelected,
                modifier = Modifier.weight(1f),
                isLoading = uiState.isLoadingLanguages
            )

            val rotation by animateFloatAsState(
                targetValue = 0f,
                label = "swap_rotation"
            )
            IconButton(
                onClick = viewModel::onSwapLanguages,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .rotate(rotation)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Swap languages",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            LanguageDropdown(
                label = "To",
                selectedLanguage = uiState.targetLanguage,
                languages = uiState.availableLanguages,
                onLanguageSelected = viewModel::onTargetLanguageSelected,
                modifier = Modifier.weight(1f),
                isLoading = uiState.isLoadingLanguages
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Source text input
        OutlinedTextField(
            value = uiState.sourceText,
            onValueChange = viewModel::onSourceTextChanged,
            label = { Text("Enter text") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Translate button
        Button(
            onClick = viewModel::translate,
            enabled = !uiState.isTranslating && uiState.sourceText.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isTranslating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (uiState.isTranslating) "Translating..." else "Translate")
        }

        // Error message
        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Translation result
        if (uiState.translatedText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Translation",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        IconButton(
                            onClick = {
                                ClipboardHelper.copyToClipboard(
                                    context,
                                    uiState.translatedText
                                )
                                Toast.makeText(
                                    context,
                                    "Copied to clipboard",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy translation",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.translatedText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Loading indicator for languages
        if (uiState.isLoadingLanguages) {
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Loading languages...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageDropdown(
    label: String,
    selectedLanguage: Language?,
    languages: List<Language>,
    onLanguageSelected: (Language) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (!isLoading) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedLanguage?.name ?: if (isLoading) "Loading..." else "Select",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            textStyle = MaterialTheme.typography.bodyMedium
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.displayName) },
                    onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    }
                )
            }
        }
    }
}
