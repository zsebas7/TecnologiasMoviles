package com.undef.superahorro.caparrozruiz.data.remote

import com.undef.superahorro.caparrozruiz.data.dto.PromotionDto
import retrofit2.http.GET

interface PromotionApiService {
    @GET("posts")
    suspend fun getPromotions(): List<PromotionDto>
}
