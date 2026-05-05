package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.undef.superahorro.caparrozruiz.data.repository.FakeWalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val city: String = ""
)

class ProfileViewModel : ViewModel() {

    private val user = FakeWalletRepository().getCurrentUser()

    private val _uiState = MutableStateFlow(
        ProfileUiState(name = user.name, email = user.email, city = user.city)
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onCityChanged(value: String) {
        _uiState.value = _uiState.value.copy(city = value)
    }
}
