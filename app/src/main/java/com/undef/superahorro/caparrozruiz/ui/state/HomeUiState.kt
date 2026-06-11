package com.undef.superahorro.caparrozruiz.ui.state

import com.undef.superahorro.caparrozruiz.data.model.Purchase

data class HomeUiState(
    val userName: String = "",
    val monthlyTotal: Double = 0.0,
    val purchases: List<Purchase> = emptyList()
)
