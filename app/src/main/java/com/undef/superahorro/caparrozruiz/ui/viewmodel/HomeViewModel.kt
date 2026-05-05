package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.data.repository.FakeWalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val userName: String = "",
    val monthlyBudget: Double = 0.0,
    val purchases: List<Purchase> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val repository = FakeWalletRepository()
    private val currentUser = repository.getCurrentUser()

    private val _uiState = MutableStateFlow(
        HomeUiState(
            userName = currentUser.name,
            monthlyBudget = currentUser.monthlyBudget,
            purchases = repository.getLatestPurchases()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
