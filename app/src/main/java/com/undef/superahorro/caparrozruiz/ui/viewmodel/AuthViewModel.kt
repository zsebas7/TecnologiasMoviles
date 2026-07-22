package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.model.User
import com.undef.superahorro.caparrozruiz.ui.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value, errorMessage = null)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val result = auth.signInWithEmailAndPassword(
                    _uiState.value.email.trim(),
                    _uiState.value.password
                ).await()
                val uid = result.user?.uid
                if (uid != null) {
                    val doc = firestore.collection("users").document(uid).get().await()
                    repository.clearAllPurchases()
                    repository.saveUser(
                        User(
                            name = doc.getString("name") ?: "",
                            email = doc.getString("email") ?: _uiState.value.email.trim(),
                            city = doc.getString("city") ?: ""
                        )
                    )
                }
                repository.setLoggedIn(true)
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = mapFirebaseError(e)
                )
            }
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val result = auth.createUserWithEmailAndPassword(
                    _uiState.value.email.trim(),
                    _uiState.value.password
                ).await()
                val uid = result.user?.uid ?: return@launch
                // Guardar perfil en Firestore (base de datos en la nube)
                firestore.collection("users").document(uid).set(
                    mapOf(
                        "name" to _uiState.value.name,
                        "email" to _uiState.value.email.trim(),
                        "city" to ""
                    )
                ).await()
                // Limpiar datos del usuario anterior y guardar el nuevo perfil localmente
                repository.clearAllPurchases()
                repository.saveUser(
                    User(
                        name = _uiState.value.name,
                        email = _uiState.value.email.trim(),
                        city = ""
                    )
                )
                repository.setLoggedIn(true)
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = mapFirebaseError(e)
                )
            }
        }
    }

    fun sendPasswordRecovery() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, forgotEmailSent = false, errorMessage = null)
            try {
                auth.sendPasswordResetEmail(_uiState.value.email.trim()).await()
                _uiState.value = _uiState.value.copy(isLoading = false, forgotEmailSent = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = mapFirebaseError(e)
                )
            }
        }
    }

    private fun mapFirebaseError(e: Exception): String = when (e) {
        is FirebaseAuthInvalidCredentialsException -> "Email o contraseña incorrectos"
        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese email"
        is FirebaseAuthWeakPasswordException -> "La contraseña debe tener al menos 6 caracteres"
        else -> "Error de autenticación. Intentá de nuevo."
    }
}
