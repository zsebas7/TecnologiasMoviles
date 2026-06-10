package com.undef.superahorro.caparrozruiz.ui.state

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val forgotEmailSent: Boolean = false,
    val isLoggedIn: Boolean = false
)
