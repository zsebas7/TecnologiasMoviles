package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.remote.RetrofitClient
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
    private val syncRepository = SyncRepository(RetrofitClient.syncApiService)

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
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, syncError = "", syncMessage = "")
            runCatching {
                val purchases = repository.observePurchases().first()
                val user = repository.observeCurrentUser().first()
                val body = buildString {
                    append("Purchases: ")
                    append(purchases.size)
                    append(", Budget: ")
                    append(user.monthlyBudget)
                }
                syncRepository.syncPurchases(
                    title = "SuperAhorro sync",
                    body = body,
                    userId = 1
                )
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
            repository.setLoggedIn(false)
            onLogoutFinished()
        }
    }
}
