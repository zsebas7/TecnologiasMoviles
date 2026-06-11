package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.dto.toDomain
import com.undef.superahorro.caparrozruiz.data.model.Promotion
import com.undef.superahorro.caparrozruiz.data.remote.PromotionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PromotionsRepository(
    private val apiService: PromotionApiService
) {
    suspend fun getPromotions(): List<Promotion> = withContext(Dispatchers.IO) {
        apiService.getPromotions().products.map { it.toDomain() }
    }
}
