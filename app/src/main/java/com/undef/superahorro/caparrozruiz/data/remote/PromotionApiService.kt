package com.undef.superahorro.caparrozruiz.data.remote

import com.undef.superahorro.caparrozruiz.data.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
//interfaz que define los endpoints de la API de promociones
//funciona como los DAOs, nosotros solo declaramos lo queremos, retrofit genera el codigo
interface PromotionApiService {
    @GET("products")
    suspend fun getPromotions(@Query("limit") limit: Int = 10): ProductsResponseDto //el json que devuelve el get se convierte en ProductsResponseDto
}
