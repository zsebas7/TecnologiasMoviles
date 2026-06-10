package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.ui.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeedData()
            repository.observePurchases().collectLatest { purchases ->
                _uiState.value = _uiState.value.copy(purchases = purchases.take(5))
            }
        }
        viewModelScope.launch {
            repository.observeCurrentUser().collectLatest { user ->
                _uiState.value = _uiState.value.copy(
                    userName = user.name,
                    monthlyBudget = user.monthlyBudget
                )
            }
        }
    }
}
