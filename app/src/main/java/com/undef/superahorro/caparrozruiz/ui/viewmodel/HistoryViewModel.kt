package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.data.repository.FakeWalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HistoryUiState(
    val purchases: List<Purchase> = emptyList(),
    val productsByPurchaseId: Map<String, List<Product>> = emptyMap(),
    val editingPurchase: Purchase? = null,
    val editingProduct: Product? = null
)

class HistoryViewModel : ViewModel() {
    private val repository = FakeWalletRepository
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.purchasesFlow().collectLatest { purchases ->
                _uiState.value = _uiState.value.copy(purchases = purchases)
            }
        }
        viewModelScope.launch {
            repository.productsByPurchaseIdFlow().collectLatest { products ->
                _uiState.value = _uiState.value.copy(productsByPurchaseId = products)
            }
        }
    }

    fun findPurchaseById(id: String): Purchase? {
        return _uiState.value.purchases.firstOrNull { it.id == id }
    }

    fun productsForPurchase(id: String): List<Product> {
        return _uiState.value.productsByPurchaseId[id].orEmpty()
    }

    fun startEditPurchase(purchaseId: String) {
        _uiState.value = _uiState.value.copy(editingPurchase = findPurchaseById(purchaseId))
    }

    fun startEditProduct(purchaseId: String, productId: String) {
        val product = productsForPurchase(purchaseId).firstOrNull { it.id == productId }
        _uiState.value = _uiState.value.copy(editingProduct = product)
    }

    fun clearEdits() {
        _uiState.value = _uiState.value.copy(editingPurchase = null, editingProduct = null)
    }

    fun updatePurchase(
        purchaseId: String,
        market: String,
        date: String,
        time: String,
        total: Double
    ) {
        val current = findPurchaseById(purchaseId) ?: return
        if (market.isBlank() || total <= 0.0) return
        repository.updatePurchase(
            current.copy(
                market = market,
                date = date,
                time = time,
                total = total
            )
        )
        clearEdits()
    }

    fun deletePurchase(purchaseId: String) {
        repository.deletePurchase(purchaseId)
        clearEdits()
    }

    fun updateProduct(
        purchaseId: String,
        productId: String,
        code: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ) {
        if (name.isBlank() || quantity <= 0 || price <= 0.0) return
        repository.updateProduct(
            purchaseId,
            Product(
                id = productId,
                code = code,
                name = name,
                description = description,
                quantity = quantity,
                price = price
            )
        )
        clearEdits()
    }

    fun deleteProduct(purchaseId: String, productId: String) {
        repository.deleteProduct(purchaseId, productId)
        clearEdits()
    }
}
