package com.undef.superahorro.caparrozruiz.ui.state

import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase

data class HistoryUiState(
    val purchases: List<Purchase> = emptyList(),
    val productsByPurchaseId: Map<Long, List<Product>> = emptyMap(),
    val editingPurchase: Purchase? = null,
    val editingProduct: Product? = null
)
