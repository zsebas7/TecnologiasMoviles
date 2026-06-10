package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.ui.state.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observePurchases().collectLatest { purchases ->
                _uiState.value = _uiState.value.copy(purchases = purchases)
            }
        }
        viewModelScope.launch {
            repository.observeProductsByPurchaseId().collectLatest { products ->
                _uiState.value = _uiState.value.copy(productsByPurchaseId = products)
            }
        }
    }

    fun findPurchaseById(id: Long): Purchase? {
        return _uiState.value.purchases.firstOrNull { it.id == id }
    }

    fun productsForPurchase(id: Long): List<Product> {
        return _uiState.value.productsByPurchaseId[id].orEmpty()
    }

    fun startEditPurchase(purchaseId: Long) {
        _uiState.value = _uiState.value.copy(editingPurchase = findPurchaseById(purchaseId))
    }

    fun startEditProduct(purchaseId: Long, productId: Long) {
        val product = productsForPurchase(purchaseId).firstOrNull { it.id == productId }
        _uiState.value = _uiState.value.copy(editingProduct = product)
    }

    fun clearEdits() {
        _uiState.value = _uiState.value.copy(editingPurchase = null, editingProduct = null)
    }

    fun updatePurchase(
        purchaseId: Long,
        market: String,
        date: String,
        time: String,
        total: Double
    ) {
        val current = findPurchaseById(purchaseId) ?: return
        if (market.isBlank() || total <= 0.0) return
        viewModelScope.launch {
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
    }

    fun deletePurchase(purchaseId: Long) {
        viewModelScope.launch {
            repository.deletePurchase(purchaseId)
            clearEdits()
        }
    }

    fun updateProduct(
        purchaseId: Long,
        productId: Long,
        code: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ) {
        if (name.isBlank() || quantity <= 0 || price <= 0.0) return
        viewModelScope.launch {
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
    }

    fun deleteProduct(purchaseId: Long, productId: Long) {
        viewModelScope.launch {
            repository.deleteProduct(purchaseId, productId)
            clearEdits()
        }
    }
}
