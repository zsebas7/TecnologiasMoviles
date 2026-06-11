package com.undef.superahorro.caparrozruiz.data.remote

import com.undef.superahorro.caparrozruiz.data.dto.OcrResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OcrApiService {
    @FormUrlEncoded
    @POST("parse/image")
    suspend fun parseImage(
        @Field("apikey") apiKey: String,
        @Field("base64Image") base64Image: String,
        @Field("language") language: String,
        @Field("OCREngine") ocrEngine: Int,
        @Field("isOverlayRequired") isOverlayRequired: Boolean
    ): OcrResponseDto
}
