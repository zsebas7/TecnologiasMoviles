package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.ui.state.NewProductUiState
import com.undef.superahorro.caparrozruiz.ui.state.NewPurchaseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NewPurchaseViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private val _purchaseState = MutableStateFlow(
        NewPurchaseUiState(
            date = LocalDate.now().format(dateFormatter),
            time = LocalTime.now().format(timeFormatter)
        )
    )
    val purchaseState: StateFlow<NewPurchaseUiState> = _purchaseState.asStateFlow()

    private val _productState = MutableStateFlow(NewProductUiState())
    val productState: StateFlow<NewProductUiState> = _productState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeDraftProducts().collectLatest { products ->
                val computedTotal = products.sumOf { it.price * it.quantity }
                _purchaseState.value = _purchaseState.value.copy(
                    products = products,
                    computedTotal = computedTotal
                )
            }
        }
    }

    fun onDateChanged(value: String) {
        _purchaseState.value = _purchaseState.value.copy(date = value)
    }

    fun onTimeChanged(value: String) {
        _purchaseState.value = _purchaseState.value.copy(time = value)
    }

    fun onMarketChanged(value: String) {
        _purchaseState.value = _purchaseState.value.copy(market = value)
    }

    fun onTotalChanged(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' }
        val normalized = if (filtered.count { it == '.' } > 1) {
            filtered.substringBefore('.') + "." + filtered.substringAfter('.', "")
        } else {
            filtered
        }
        _purchaseState.value = _purchaseState.value.copy(total = normalized)
    }

    fun onProductNameChanged(value: String) {
        _productState.value = _productState.value.copy(name = value)
    }

    fun onProductDescriptionChanged(value: String) {
        _productState.value = _productState.value.copy(description = value)
    }

    fun onProductQuantityChanged(value: String) {
        _productState.value = _productState.value.copy(quantity = value)
    }

    fun onProductPriceChanged(value: String) {
        _productState.value = _productState.value.copy(price = value)
    }

    fun setTicketStatus(value: String) {
        _purchaseState.value = _purchaseState.value.copy(ticketStatus = value, ticketUri = value)
    }

    fun addProduct(onAdded: () -> Unit) {
        val quantity = _productState.value.quantity.toIntOrNull() ?: 0
        val price = _productState.value.price.toDoubleOrNull() ?: 0.0
        if (_productState.value.name.isBlank() || quantity <= 0 || price <= 0.0) return
        val product = Product(
            id = System.currentTimeMillis(),
            code = "",
            name = _productState.value.name,
            description = _productState.value.description,
            quantity = quantity,
            price = price
        )
        viewModelScope.launch {
            repository.addDraftProduct(product)
            val computedTotal = _purchaseState.value.products.sumOf { it.price * it.quantity }
            _purchaseState.value = _purchaseState.value.copy(
                total = "%.2f".format(computedTotal),
                computedTotal = computedTotal
            )
            _productState.value = NewProductUiState()
            onAdded()
        }
    }

    fun removeDraftProduct(productId: Long) {
        viewModelScope.launch {
            repository.removeDraftProduct(productId)
            val computedTotal = _purchaseState.value.products.sumOf { it.price * it.quantity }
            _purchaseState.value = _purchaseState.value.copy(
                total = if (computedTotal > 0.0) "%.2f".format(computedTotal) else "",
                computedTotal = computedTotal
            )
        }
    }

    fun savePurchase(onSaved: () -> Unit) {
        viewModelScope.launch {
            _purchaseState.value = _purchaseState.value.copy(isSaving = true)
            val totalValue = _purchaseState.value.total.toDoubleOrNull()
                ?: _purchaseState.value.computedTotal
            if (_purchaseState.value.market.isBlank() || totalValue <= 0.0) {
                _purchaseState.value = _purchaseState.value.copy(isSaving = false)
                return@launch
            }
            val purchase = Purchase(
                id = 0,
                market = _purchaseState.value.market,
                date = _purchaseState.value.date,
                time = _purchaseState.value.time,
                total = totalValue,
                ticketUri = _purchaseState.value.ticketUri
            )
            repository.addPurchase(purchase, _purchaseState.value.products)
            _purchaseState.value = NewPurchaseUiState(
                date = LocalDate.now().format(dateFormatter),
                time = LocalTime.now().format(timeFormatter)
            )
            onSaved()
        }
    }
}
