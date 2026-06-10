package com.undef.superahorro.caparrozruiz.ui.state

import com.undef.superahorro.caparrozruiz.data.model.Promotion

data class PromotionsUiState(
    val isLoading: Boolean = false,
    val promotions: List<Promotion> = emptyList(),
    val errorMessage: String? = null
)
