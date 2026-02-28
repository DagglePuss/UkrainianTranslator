package com.ukrainiantranslator.data.remote

import com.ukrainiantranslator.data.remote.dto.LanguageDto
import com.ukrainiantranslator.data.remote.dto.TranslateRequest
import com.ukrainiantranslator.data.remote.dto.TranslateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LibreTranslateApi {

    @POST("/translate")
    suspend fun translate(@Body request: TranslateRequest): TranslateResponse

    @GET("/languages")
    suspend fun getLanguages(): List<LanguageDto>
}
