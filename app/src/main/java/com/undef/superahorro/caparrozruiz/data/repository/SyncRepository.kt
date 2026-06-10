package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.dto.SyncPurchaseDto
import com.undef.superahorro.caparrozruiz.data.remote.SyncApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncRepository(
    private val apiService: SyncApiService
) {
    suspend fun syncPurchases(title: String, body: String, userId: Int): String = withContext(Dispatchers.IO) {
        val response = apiService.syncPurchase(
            SyncPurchaseDto(
                title = title,
                body = body,
                userId = userId
            )
        )
        "Sync ok: #${response.id}"
    }
}
