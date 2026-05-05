package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.undef.superahorro.caparrozruiz.data.model.ChatMessage
import com.undef.superahorro.caparrozruiz.data.repository.FakeWalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChatUiState(
    val input: String = "",
    val messages: List<ChatMessage> = emptyList()
)

class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState(messages = FakeWalletRepository.getInitialChatMessages())
    )
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputChanged(value: String) {
        _uiState.value = _uiState.value.copy(input = value)
    }

    fun sendMessage() {
        val text = _uiState.value.input.trim()
        if (text.isEmpty()) return

        val userMessage = ChatMessage(id = "u-${System.currentTimeMillis()}", message = text, isFromUser = true)
        val botMessage = ChatMessage(
            id = "b-${System.currentTimeMillis()}",
            message = "Registrado. En base a tus compras recientes, conviene revisar promociones semanales.",
            isFromUser = false
        )

        _uiState.value = _uiState.value.copy(
            input = "",
            messages = _uiState.value.messages + userMessage + botMessage
        )
    }
}
