package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val purchases: List<Purchase> = emptyList(),
    val productsByPurchaseId: Map<String, List<Product>> = emptyMap()
)

class HistoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = HistoryUiState(
                purchases = mockPurchases(),
                productsByPurchaseId = mockProducts()
            )
        }
    }

    fun findPurchaseById(id: String): Purchase? {
        return _uiState.value.purchases.firstOrNull { it.id == id }
    }

    fun productsForPurchase(id: String): List<Product> {
        return _uiState.value.productsByPurchaseId[id].orEmpty()
    }

    private fun mockPurchases(): List<Purchase> {
        return listOf(
            Purchase(id = "1", market = "Carrefour", date = "2026-05-01", total = 38650.0),
            Purchase(id = "2", market = "Disco", date = "2026-04-29", total = 23400.0),
            Purchase(id = "3", market = "Coto", date = "2026-04-27", total = 41200.0),
            Purchase(id = "4", market = "Dia", date = "2026-04-24", total = 15780.0)
        )
    }

    private fun mockProducts(): Map<String, List<Product>> {
        return mapOf(
            "1" to listOf(
                Product("1", "779123", "Arroz largo", "Bolsa 1kg", 2, 1800.0),
                Product("2", "779456", "Aceite", "Girasol 900ml", 1, 3200.0),
                Product("3", "779789", "Yerba", "Paquete 500g", 3, 2400.0)
            ),
            "2" to listOf(
                Product("4", "779111", "Leche", "Entera 1L", 4, 1100.0),
                Product("5", "779222", "Pan", "Pan lactal", 1, 950.0)
            ),
            "3" to listOf(
                Product("6", "779333", "Fideos", "Spaghetti 500g", 2, 1250.0),
                Product("7", "779444", "Salsa", "Tomate 340g", 2, 980.0),
                Product("8", "779555", "Queso", "Cremoso 300g", 1, 3600.0)
            ),
            "4" to listOf(
                Product("9", "779666", "Galletas", "Chocolate 250g", 2, 1400.0),
                Product("10", "779777", "Jugo", "Naranja 1.5L", 1, 1700.0)
            ),
            "5" to listOf(
                Product("11", "779888", "Azucar", "Bolsa 1kg", 1, 1650.0),
                Product("12", "779999", "Cafe", "Instantaneo 170g", 1, 3950.0),
                Product("13", "779000", "Manteca", "200g", 2, 2200.0)
            )
        )
    }
}
