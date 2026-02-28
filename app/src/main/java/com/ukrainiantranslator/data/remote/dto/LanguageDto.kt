package com.ukrainiantranslator.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LanguageDto(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("targets") val targets: List<String>
)
