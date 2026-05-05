package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val forgotEmailSent: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1200)
            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess()
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1200)
            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess()
        }
    }

    fun sendPasswordRecovery() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, forgotEmailSent = false)
            delay(1200)
            _uiState.value = _uiState.value.copy(isLoading = false, forgotEmailSent = true)
        }
    }
}
