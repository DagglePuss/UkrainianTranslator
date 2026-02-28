package com.ukrainiantranslator.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TranslateResponse(
    @SerializedName("translatedText") val translatedText: String,
    @SerializedName("detectedLanguage") val detectedLanguage: DetectedLanguage? = null
)

data class DetectedLanguage(
    @SerializedName("language") val language: String,
    @SerializedName("confidence") val confidence: Double
)
