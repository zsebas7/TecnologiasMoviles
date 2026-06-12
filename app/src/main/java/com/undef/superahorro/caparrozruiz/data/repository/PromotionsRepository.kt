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
    //implementa el patron offline-first, es decir, usa Room como caché para guardar los datos y poder mostrarlos
    //si se pierde la conexion a internet
    private val apiService: PromotionApiService,
    private val promotionDao: PromotionDao
) {
    //devuelve un Flow de Room, la UI se suscribe a esto y muestra lo que hay en caché local
    fun observePromotions(): Flow<List<Promotion>> =
        promotionDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun refreshPromotions(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiService.getPromotions() //llama a la API
            val entities = response.products.map { it.toDomain().toEntity() }//convierte DTO a Entity
            promotionDao.deleteAll() //limpia caché vieja
            promotionDao.insertAll(entities) //guarda los datos nuevos
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
