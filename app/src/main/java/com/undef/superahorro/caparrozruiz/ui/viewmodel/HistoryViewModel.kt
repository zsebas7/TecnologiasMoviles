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
    val productsByPurchaseId: Map<String, List<Product>> = emptyMap()
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

    
}
