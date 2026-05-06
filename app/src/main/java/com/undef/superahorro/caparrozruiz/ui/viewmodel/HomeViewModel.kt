package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.data.repository.FakeWalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "",
    val monthlyBudget: Double = 0.0,
    val purchases: List<Purchase> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val repository = FakeWalletRepository
    private val currentUser = repository.getCurrentUser()

    private val _uiState = MutableStateFlow(
        HomeUiState(
            userName = currentUser.name,
            monthlyBudget = currentUser.monthlyBudget,
            purchases = emptyList()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.purchasesFlow().collectLatest { purchases ->
                _uiState.value = _uiState.value.copy(purchases = purchases.take(5))
            }
        }
    }
}
