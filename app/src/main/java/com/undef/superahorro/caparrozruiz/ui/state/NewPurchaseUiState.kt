package com.undef.superahorro.caparrozruiz.ui.state

import com.undef.superahorro.caparrozruiz.data.model.Product

data class NewPurchaseUiState(
    val date: String = "",
    val time: String = "",
    val market: String = "",
    val total: String = "",
    val computedTotal: Double = 0.0,
    val products: List<Product> = emptyList(),
    val isSaving: Boolean = false,
    val ticketStatus: String = "",
    val ticketUri: String = "",
    val isAnalyzingTicket: Boolean = false,
    val ocrError: String? = null
)
