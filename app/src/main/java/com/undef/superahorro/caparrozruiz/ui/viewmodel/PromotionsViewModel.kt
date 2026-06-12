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
    //le pasa al reposiroty el cliente HTTP y el DAO de Room
    private val repository = PromotionsRepository(
        RetrofitClient.promotionApiService, //Retrofit provee conexion
        AppContainer.database.promotionDao() //Appcontainter provee base de datos
    )

    private val _uiState = MutableStateFlow(PromotionsUiState(isLoading = true))
    val uiState: StateFlow<PromotionsUiState> = _uiState.asStateFlow()

    init {
        observeCachedPromotions() //se suscribe al Flow de Room y muestra lo que hay guardado
        refreshFromNetwork() //va a buscar datos nuevos a la API
    }

    private fun observeCachedPromotions() {
        viewModelScope.launch {
            //se suscribe al flow con collectLatest para que cada vez que Room tenga datos nuevos la UI se actualice
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
