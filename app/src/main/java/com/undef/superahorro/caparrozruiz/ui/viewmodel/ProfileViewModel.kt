package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.model.User
import com.undef.superahorro.caparrozruiz.ui.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeCurrentUser().collectLatest { user ->
                _uiState.value = _uiState.value.copy(
                    name = user.name,
                    email = user.email,
                    city = user.city,
                    monthlyBudget = user.monthlyBudget.toString()
                )
            }
        }
    }

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onCityChanged(value: String) {
        _uiState.value = _uiState.value.copy(city = value)
    }

    fun onMonthlyBudgetChanged(value: String) {
        val normalized = value.filter { it.isDigit() || it == '.' }
        _uiState.value = _uiState.value.copy(monthlyBudget = normalized)
    }

    fun saveProfile() {
        viewModelScope.launch {
            val budget = _uiState.value.monthlyBudget.toDoubleOrNull() ?: 0.0
            if (_uiState.value.name.isBlank() || _uiState.value.email.isBlank()) return@launch
            repository.saveUser(
                User(
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    city = _uiState.value.city,
                    monthlyBudget = budget
                )
            )
        }
    }
}
