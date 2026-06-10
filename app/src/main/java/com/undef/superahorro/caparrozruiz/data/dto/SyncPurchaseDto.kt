package com.undef.superahorro.caparrozruiz.data.dto

import com.google.gson.annotations.SerializedName

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
