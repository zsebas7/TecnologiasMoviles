package com.undef.superahorro.caparrozruiz.data.dto

import com.google.gson.annotations.SerializedName
import com.undef.superahorro.caparrozruiz.data.model.Promotion

data class ProductsResponseDto(
    @SerializedName("products") val products: List<PromotionDto>
)

data class PromotionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("discountPercentage") val discountPercentage: Double,
    @SerializedName("category") val category: String
)

fun PromotionDto.toDomain(): Promotion = Promotion(
    id = id,
    title = "$title — ${discountPercentage.toInt()}% OFF",
    description = "USD ${"%.2f".format(price)} · $description"
)
