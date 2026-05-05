package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.data.model.Product
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.data.repository.FakeWalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class NewPurchaseUiState(
    val date: String = "",
    val time: String = "",
    val market: String = "",
    val total: String = "",
    val products: List<Product> = emptyList(),
    val isSaving: Boolean = false
)

data class NewProductUiState(
    val name: String = "",
    val quantity: String = "",
    val price: String = ""
)

class NewPurchaseViewModel : ViewModel() {
    private val repository = FakeWalletRepository

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
            repository.draftProductsFlow().collectLatest { products ->
                _purchaseState.value = _purchaseState.value.copy(products = products)
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
        _purchaseState.value = _purchaseState.value.copy(total = value)
    }

    fun onProductNameChanged(value: String) {
        _productState.value = _productState.value.copy(name = value)
    }

    fun onProductQuantityChanged(value: String) {
        _productState.value = _productState.value.copy(quantity = value)
    }

    fun onProductPriceChanged(value: String) {
        _productState.value = _productState.value.copy(price = value)
    }

    fun addProduct(onAdded: () -> Unit) {
        val quantity = _productState.value.quantity.toIntOrNull() ?: 0
        val price = _productState.value.price.toDoubleOrNull() ?: 0.0
        val product = Product(
            id = "DRAFT-${System.currentTimeMillis()}",
            code = "",
            name = _productState.value.name,
            description = "",
            quantity = quantity,
            price = price
        )
        repository.addDraftProduct(product)
        _productState.value = NewProductUiState()
        onAdded()
    }

    fun removeDraftProduct(productId: String) {
        repository.removeDraftProduct(productId)
    }

    fun savePurchase(onSaved: () -> Unit) {
        viewModelScope.launch {
            _purchaseState.value = _purchaseState.value.copy(isSaving = true)
            val totalValue = _purchaseState.value.total.toDoubleOrNull() ?: 0.0
            val purchase = Purchase(
                id = "P-${System.currentTimeMillis()}",
                market = _purchaseState.value.market,
                date = _purchaseState.value.date,
                total = totalValue
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
