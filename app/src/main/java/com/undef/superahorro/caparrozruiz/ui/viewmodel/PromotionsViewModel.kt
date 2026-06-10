package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.data.remote.RetrofitClient
import com.undef.superahorro.caparrozruiz.data.repository.PromotionsRepository
import com.undef.superahorro.caparrozruiz.ui.state.PromotionsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PromotionsViewModel : ViewModel() {
    private val repository = PromotionsRepository(RetrofitClient.promotionApiService)

    private val _uiState = MutableStateFlow(PromotionsUiState(isLoading = true))
    val uiState: StateFlow<PromotionsUiState> = _uiState.asStateFlow()

    init {
        loadPromotions()
    }

    fun loadPromotions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { repository.getPromotions() }
                .onSuccess { promotions ->
                    _uiState.value = PromotionsUiState(promotions = promotions)
                }
                .onFailure { throwable ->
                    _uiState.value = PromotionsUiState(
                        errorMessage = throwable.message ?: "Error desconocido"
                    )
                }
        }
    }
}
