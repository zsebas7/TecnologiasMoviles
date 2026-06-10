package com.undef.superahorro.caparrozruiz.ui.state

import com.undef.superahorro.caparrozruiz.data.model.ChatMessage

data class ChatUiState(
    val input: String = "",
    val messages: List<ChatMessage> = emptyList()
)
