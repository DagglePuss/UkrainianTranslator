package com.ukrainiantranslator.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TranslateRequest(
    @SerializedName("q") val text: String,
    @SerializedName("source") val sourceLanguage: String,
    @SerializedName("target") val targetLanguage: String,
    @SerializedName("format") val format: String = "text",
    @SerializedName("api_key") val apiKey: String = ""
)
