package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.model.User
import com.undef.superahorro.caparrozruiz.ui.state.AuthUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeIsLoggedIn().collectLatest { loggedIn ->
                _uiState.value = _uiState.value.copy(isLoggedIn = loggedIn)
            }
        }
    }

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
            repository.setLoggedIn(true)
            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess()
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1200)
            if (_uiState.value.name.isNotBlank() && _uiState.value.email.isNotBlank()) {
                repository.saveUser(
                    User(
                        name = _uiState.value.name,
                        email = _uiState.value.email,
                        city = ""
                    )
                )
            }
            repository.setLoggedIn(true)
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
