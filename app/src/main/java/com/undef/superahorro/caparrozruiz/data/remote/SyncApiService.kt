package com.undef.superahorro.caparrozruiz.data.remote

import com.undef.superahorro.caparrozruiz.data.dto.SyncPurchaseDto
import com.undef.superahorro.caparrozruiz.data.dto.SyncPurchaseResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface SyncApiService {
    @POST("posts")
    suspend fun syncPurchase(@Body request: SyncPurchaseDto): SyncPurchaseResponseDto
}
