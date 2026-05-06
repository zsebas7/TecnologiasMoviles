package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.model.ChatMessage
import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FakeWalletRepository {

    private val user = User(
        name = "Martin Perez",
        email = "martin.perez@email.com",
        city = "Cordoba",
        monthlyBudget = 265000.0
    )

    private val initialPurchases = listOf(
        Purchase(id = "P001", market = "Carrefour", date = "2026-05-01", total = 38650.0),
        Purchase(id = "P002", market = "Disco", date = "2026-04-29", total = 23400.0),
        Purchase(id = "P003", market = "Coto", date = "2026-04-27", total = 41200.0),
        Purchase(id = "P004", market = "Dia", date = "2026-04-24", total = 15780.0)
    )

    private val initialProducts = mapOf(
        "P001" to listOf(
            Product("1", "779123", "Arroz largo", "Bolsa 1kg", 2, 1800.0),
            Product("2", "779456", "Aceite", "Girasol 900ml", 1, 3200.0)
        ),
        "P002" to listOf(
            Product("3", "779111", "Leche", "Entera 1L", 4, 1100.0)
        ),
        "P003" to listOf(
            Product("4", "779333", "Fideos", "Spaghetti 500g", 2, 1250.0),
            Product("5", "779555", "Queso", "Cremoso 300g", 1, 3600.0)
        )
    )

    private val purchasesState = MutableStateFlow(initialPurchases)
    private val productsState = MutableStateFlow(initialProducts)
    private val draftProductsState = MutableStateFlow<List<Product>>(emptyList())

    fun getCurrentUser(): User = user

    fun purchasesFlow(): StateFlow<List<Purchase>> = purchasesState.asStateFlow()

    fun productsByPurchaseIdFlow(): StateFlow<Map<String, List<Product>>> = productsState.asStateFlow()

    fun draftProductsFlow(): StateFlow<List<Product>> = draftProductsState.asStateFlow()

    fun addDraftProduct(product: Product) {
        draftProductsState.value = draftProductsState.value + product
    }

    fun removeDraftProduct(productId: String) {
        draftProductsState.value = draftProductsState.value.filterNot { it.id == productId }
    }

    fun clearDraftProducts() {
        draftProductsState.value = emptyList()
    }

    fun addPurchase(purchase: Purchase, products: List<Product>) {
        purchasesState.value = listOf(purchase) + purchasesState.value
        if (products.isNotEmpty()) {
            productsState.value = productsState.value + (purchase.id to products)
        }
        clearDraftProducts()
    }

    fun getInitialChatMessages(): List<ChatMessage> {
        return listOf(
            ChatMessage(id = "m1", message = "Hola, soy tu asistente de consumos.", isFromUser = false),
            ChatMessage(id = "m2", message = "Podés preguntar por precios o historial.", isFromUser = false)
        )
    }
}
