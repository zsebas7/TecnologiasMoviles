package com.undef.superahorro.caparrozruiz.data.remote

import com.undef.superahorro.caparrozruiz.data.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PromotionApiService {
    @GET("products")
    suspend fun getPromotions(@Query("limit") limit: Int = 10): ProductsResponseDto
}
