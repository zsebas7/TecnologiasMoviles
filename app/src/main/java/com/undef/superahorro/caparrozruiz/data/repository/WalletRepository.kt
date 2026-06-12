package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.data.model.User
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun observePurchases(): Flow<List<Purchase>>
    fun observeProductsByPurchaseId(): Flow<Map<Long, List<Product>>>
    fun observeDraftProducts(): Flow<List<Product>>
    fun observeCurrentUser(): Flow<User>
    fun observeIsLoggedIn(): Flow<Boolean>
    fun observeNotificationsEnabled(): Flow<Boolean>
    fun observeMonthlySummaryEnabled(): Flow<Boolean>

    suspend fun addDraftProduct(product: Product)
    suspend fun removeDraftProduct(productId: Long)
    suspend fun clearDraftProducts()
    suspend fun addPurchase(purchase: Purchase, products: List<Product>)
    suspend fun updatePurchase(updated: Purchase)
    suspend fun deletePurchase(purchaseId: Long)
    suspend fun updateProduct(purchaseId: Long, updated: Product)
    suspend fun deleteProduct(purchaseId: Long, productId: Long)
    suspend fun saveUser(user: User)
    suspend fun setLoggedIn(loggedIn: Boolean)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setMonthlySummaryEnabled(enabled: Boolean)
}
