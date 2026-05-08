package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.data.repository.FakeWalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//Agrupar los datos en una data class ayuda a la UI a reaccionar a un solo objeto
data class HomeUiState(
    val userName: String = "",
    val monthlyBudget: Double = 0.0,
    val purchases: List<Purchase> = emptyList()
)

class HomeViewModel : ViewModel() {
    //Referencia al repository, ViewModel no sabe como se guardan los datos, solo los pide
    private val repository = FakeWalletRepository
    private val currentUser = repository.getCurrentUser()

    //Privado para que no se pueda modificar desde fuera
    private val _uiState = MutableStateFlow(
        HomeUiState( //Inicializado con datos basicos del usuario
            userName = currentUser.name,
            monthlyBudget = currentUser.monthlyBudget,
            purchases = emptyList()
        )
    )

    //Version de solo lectura para la UI
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            //Si los datos del repository cambian, se actualiza el estado
            repository.purchasesFlow().collectLatest { purchases ->

                _uiState.value = _uiState.value.copy(purchases = purchases.take(5))

            }
        }
    }
}
