package com.undef.superahorro.caparrozruiz.data.remote

import com.undef.superahorro.caparrozruiz.data.dto.GeminiRequest
import com.undef.superahorro.caparrozruiz.data.dto.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemma-4-26b-a4b-it:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
