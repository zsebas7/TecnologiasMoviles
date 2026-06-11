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
                    city = user.city
                )
            }
        }
    }

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value, saved = false)
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value, saved = false)
    }

    fun onCityChanged(value: String) {
        _uiState.value = _uiState.value.copy(city = value, saved = false)
    }

    fun saveProfile() {
        viewModelScope.launch {
            if (_uiState.value.name.isBlank() || _uiState.value.email.isBlank()) return@launch
            repository.saveUser(
                User(
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    city = _uiState.value.city
                )
            )
            _uiState.value = _uiState.value.copy(saved = true)
        }
    }
}
