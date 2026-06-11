package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.dto.toDomain
import com.undef.superahorro.caparrozruiz.data.local.dao.PromotionDao
import com.undef.superahorro.caparrozruiz.data.local.entity.PromotionEntity
import com.undef.superahorro.caparrozruiz.data.model.Promotion
import com.undef.superahorro.caparrozruiz.data.remote.PromotionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PromotionsRepository(
    private val apiService: PromotionApiService,
    private val promotionDao: PromotionDao
) {
    fun observePromotions(): Flow<List<Promotion>> =
        promotionDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun refreshPromotions(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiService.getPromotions()
            val entities = response.products.map { it.toDomain().toEntity() }
            promotionDao.deleteAll()
            promotionDao.insertAll(entities)
        }
    }

    private fun PromotionEntity.toDomain(): Promotion = Promotion(
        id = id,
        title = title,
        description = description
    )

    private fun Promotion.toEntity(): PromotionEntity = PromotionEntity(
        id = id,
        title = title,
        description = description
    )
}
