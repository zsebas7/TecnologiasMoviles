package com.undef.superahorro.caparrozruiz.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.model.ChatMessage
import com.undef.superahorro.caparrozruiz.data.remote.RetrofitClient
import com.undef.superahorro.caparrozruiz.data.repository.ChatRepository
import com.undef.superahorro.caparrozruiz.data.repository.ChatStateHolder
import com.undef.superahorro.caparrozruiz.ui.state.ChatUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository(RetrofitClient.geminiApiService)

    private val _uiState = MutableStateFlow(
        ChatUiState(messages = ChatStateHolder.messages.toList())
    )
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputChanged(value: String) {
        _uiState.value = _uiState.value.copy(input = value)
    }

    fun sendMessage() {
        val text = _uiState.value.input.trim()
        if (text.isEmpty()) return

        val userMessage = ChatMessage(id = "u-${System.currentTimeMillis()}", message = text, isFromUser = true)
        ChatStateHolder.addMessage(userMessage)
        _uiState.value = _uiState.value.copy(input = "", messages = ChatStateHolder.messages.toList(), isLoading = true)

        viewModelScope.launch {
            val contextData = withContext(Dispatchers.IO) {
                val database = AppContainer.database
                val promotions = database.promotionDao().getAll().first()
                val purchases = database.purchaseDao().observeAll().first()
                val allProducts = database.productDao().observeAll().first()
                val productsByPurchase = allProducts.groupBy { it.purchaseId }
                buildString {
                    appendLine("Promociones disponibles: ${if (promotions.isNotEmpty()) "Sí. Revisalas en la sección Promociones de la app." else "No hay promociones activas."}")
                    appendLine()
                    appendLine("Historial de compras (total: ${purchases.size} compras):")
                    purchases.forEach { p ->
                        appendLine("  ${p.market} - ${p.date} ${p.time} - Total: $${p.total}")
                    }
                    appendLine()
                    val lastPurchases = purchases.take(3)
                    if (lastPurchases.isNotEmpty()) {
                        appendLine("Detalle de las últimas 3 compras:")
                        lastPurchases.forEach { p ->
                            appendLine("  ${p.market} - ${p.date}:")
                            val products = productsByPurchase[p.id].orEmpty()
                            if (products.isNotEmpty()) {
                                products.forEach { prod ->
                                    appendLine("    - ${prod.name} x${prod.quantity} = $${prod.price} c/u")
                                }
                            }
                        }
                    }
                }
            }
            val botText = runCatching { repository.sendMessage(text, contextData) }
                .onFailure { Log.e("ChatVM", "Error con Gemini", it) }
                .getOrDefault("Error al conectar con el asistente.")

            val botMessage = ChatMessage(
                id = "b-${System.currentTimeMillis()}",
                message = botText,
                isFromUser = false
            )
            ChatStateHolder.addMessage(botMessage)
            _uiState.value = _uiState.value.copy(
                messages = ChatStateHolder.messages.toList(),
                isLoading = false
            )
        }
    }
}
