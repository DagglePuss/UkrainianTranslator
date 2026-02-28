package com.ukrainiantranslator.domain.model

data class Language(
    val code: String,
    val name: String,
    val targets: List<String>
) {
    val displayName: String
        get() = "$name ($code)"
}
