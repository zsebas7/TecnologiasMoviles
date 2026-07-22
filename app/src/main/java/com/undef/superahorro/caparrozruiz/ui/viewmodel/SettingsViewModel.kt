package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.dto.SyncProductDto
import com.undef.superahorro.caparrozruiz.data.dto.SyncPurchaseItemDto
import com.undef.superahorro.caparrozruiz.data.repository.SyncRepository
import com.undef.superahorro.caparrozruiz.ui.state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository
    private val syncRepository = SyncRepository()

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeNotificationsEnabled().collectLatest { enabled ->
                _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
            }
        }
        viewModelScope.launch {
            repository.observeMonthlySummaryEnabled().collectLatest { enabled ->
                _uiState.value = _uiState.value.copy(monthlySummaryEnabled = enabled)
            }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNotificationsEnabled(enabled)
        }
    }

    fun setMonthlySummaryEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setMonthlySummaryEnabled(enabled)
        }
    }

    fun syncPurchases() {
        //del boton de sincronizar compra con la nube
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, syncError = "", syncMessage = "")
            runCatching {
                val purchases = repository.observePurchases().first()
                val productsByPurchase = repository.observeProductsByPurchaseId().first()
                //.first() toma el valor actual del Flow sin quedarse suscripto

                //armar los DTOs
                //convierte los objetos Purchase y Product a objetos dise!nados para ser enviados a la API/Firestore
                val syncItems = purchases.map { purchase ->
                    SyncPurchaseItemDto(
                        id = purchase.id,
                        market = purchase.market,
                        date = purchase.date,
                        time = purchase.time,
                        total = purchase.total,
                        products = productsByPurchase[purchase.id]?.map { product ->
                            SyncProductDto(
                                name = product.name,
                                quantity = product.quantity,
                                price = product.price
                            )
                        } ?: emptyList()
                    )
                }
                syncRepository.syncPurchases(purchases = syncItems)
            }.onSuccess { message ->
                _uiState.value = _uiState.value.copy(isSyncing = false, syncMessage = message)
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    syncError = throwable.message ?: "Error desconocido"
                )
            }
        }
    }

    fun logout(onLogoutFinished: () -> Unit) {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            repository.clearAllPurchases()
            repository.setLoggedIn(false)
            onLogoutFinished()
        }
    }
}
