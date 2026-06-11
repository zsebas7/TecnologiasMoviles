package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.remote.RetrofitClient
import com.undef.superahorro.caparrozruiz.data.repository.PromotionsRepository
import com.undef.superahorro.caparrozruiz.ui.state.PromotionsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PromotionsViewModel : ViewModel() {
    private val repository = PromotionsRepository(
        RetrofitClient.promotionApiService,
        AppContainer.database.promotionDao()
    )

    private val _uiState = MutableStateFlow(PromotionsUiState(isLoading = true))
    val uiState: StateFlow<PromotionsUiState> = _uiState.asStateFlow()

    init {
        observeCachedPromotions()
        refreshFromNetwork()
    }

    private fun observeCachedPromotions() {
        viewModelScope.launch {
            repository.observePromotions().collectLatest { promotions ->
                _uiState.value = _uiState.value.copy(
                    promotions = promotions,
                    isLoading = false
                )
            }
        }
    }

    fun refreshFromNetwork() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, refreshError = null)
            repository.refreshPromotions()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, refreshError = null)
                }
                .onFailure { throwable ->
                    val current = _uiState.value
                    if (current.promotions.isEmpty()) {
                        _uiState.value = current.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Error de conexión"
                        )
                    } else {
                        _uiState.value = current.copy(
                            isLoading = false,
                            refreshError = "No se pudo actualizar: ${throwable.message ?: "Error de conexión"}"
                        )
                    }
                }
        }
    }
}
