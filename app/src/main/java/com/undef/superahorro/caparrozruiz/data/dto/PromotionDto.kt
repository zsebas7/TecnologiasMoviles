package com.undef.superahorro.caparrozruiz.data.dto

import com.google.gson.annotations.SerializedName
import com.undef.superahorro.caparrozruiz.data.model.Promotion

data class PromotionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)

fun PromotionDto.toDomain(): Promotion = Promotion(
    id = id,
    title = title,
    description = body
)
