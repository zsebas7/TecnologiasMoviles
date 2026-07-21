package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.local.dao.ProductDao
import com.undef.superahorro.caparrozruiz.data.local.dao.PurchaseDao
import com.undef.superahorro.caparrozruiz.data.local.entity.ProductEntity
import com.undef.superahorro.caparrozruiz.data.local.entity.PurchaseEntity
import com.undef.superahorro.caparrozruiz.data.local.preferences.UserPreferencesDataSource
import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

//Intermediario entre los viewModel y las fuentes de datos.
//Los viewmodel hablan con DefaultWalletRepository en vez de con los DAOs o con UserPreferencesDataSource
class DefaultWalletRepository(
    private val purchaseDao: PurchaseDao,
    private val productDao: ProductDao,
    private val preferences: UserPreferencesDataSource
) : WalletRepository {

    private val draftProductsState = MutableStateFlow<List<Product>>(emptyList())
    //Leer Room y convertir:
    //Rom devuelve List<PurchaseEntity> y .map los convierte a List<Purchase>
    override fun observePurchases(): Flow<List<Purchase>> =
        purchaseDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeProductsByPurchaseId(): Flow<Map<Long, List<Product>>> =
        productDao.observeAll().map { entities ->
            entities.groupBy { it.purchaseId }.mapValues { (_, products) ->
                products.map { it.toDomain() }
            }
        }

    //expone los Flows de UserPreferencesDataSource para que el viewModel lo vea
    override fun observeCurrentUser(): Flow<User> = preferences.currentUser
    override fun observeDraftProducts(): Flow<List<Product>> = draftProductsState
    override fun observeIsLoggedIn(): Flow<Boolean> = preferences.isLoggedIn
    override fun observeNotificationsEnabled(): Flow<Boolean> = preferences.notificationsEnabled
    override fun observeMonthlySummaryEnabled(): Flow<Boolean> = preferences.monthlySummaryEnabled

    override suspend fun addDraftProduct(product: Product) {
        draftProductsState.value = draftProductsState.value + product
    }

    override suspend fun removeDraftProduct(productId: Long) {
        draftProductsState.value = draftProductsState.value.filterNot { it.id == productId }
    }

    override suspend fun clearDraftProducts() {
        draftProductsState.value = emptyList()
    }

    //insterta la compra primero para obtener el id generado por Room,
    //despues vincula los productos a ese ID
    override suspend fun addPurchase(purchase: Purchase, products: List<Product>) {
        val purchaseId = purchaseDao.insert(purchase.toEntity())
        if (products.isNotEmpty()) {
            productDao.insertAll(products.map { it.toEntity(purchaseId) })
        }
        clearDraftProducts()
    }

    override suspend fun updatePurchase(updated: Purchase) {
        purchaseDao.update(updated.toEntity())
    }

    override suspend fun deletePurchase(purchaseId: Long) {
        purchaseDao.deleteById(purchaseId)
    }

    override suspend fun updateProduct(purchaseId: Long, updated: Product) {
        productDao.update(updated.toEntity(purchaseId))
    }

    override suspend fun deleteProduct(purchaseId: Long, productId: Long) {
        productDao.deleteById(productId)
    }

    override suspend fun clearAllPurchases() {
        productDao.deleteAll()
        purchaseDao.deleteAll()
    }

    override suspend fun saveUser(user: User) {
        preferences.saveUser(user)
    }

    override suspend fun setLoggedIn(loggedIn: Boolean) {
        preferences.setLoggedIn(loggedIn)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        preferences.setNotificationsEnabled(enabled)
    }

    override suspend fun setMonthlySummaryEnabled(enabled: Boolean) {
        preferences.setMonthlySummaryEnabled(enabled)
    }

    private fun PurchaseEntity.toDomain(): Purchase = Purchase(
        id = id,
        market = market,
        date = date,
        time = time,
        total = total,
        ticketUri = ticketUri
    )

    private fun Purchase.toEntity(): PurchaseEntity = PurchaseEntity(
        id = id,
        market = market,
        date = date,
        time = time,
        total = total,
        ticketUri = ticketUri
    )

    private fun ProductEntity.toDomain(): Product = Product(
        id = id,
        code = code,
        name = name,
        description = description,
        quantity = quantity,
        price = price
    )

    private fun Product.toEntity(purchaseId: Long): ProductEntity = ProductEntity(
        id = id,
        purchaseId = purchaseId,
        code = code,
        name = name,
        description = description,
        quantity = quantity,
        price = price
    )
}
