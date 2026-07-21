package com.undef.superahorro.caparrozruiz.data.dto

import com.google.gson.annotations.SerializedName

data class SyncProductDto(
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double
)

data class SyncPurchaseItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("market") val market: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("total") val total: Double,
    @SerializedName("products") val products: List<SyncProductDto>
)

data class SyncPurchaseDto(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("userId") val userId: Int
)

data class SyncPurchaseResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("userId") val userId: Int
)
