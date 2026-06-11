package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.model.ChatMessage

object ChatStateHolder {
    val messages = mutableListOf(
        ChatMessage(id = "m1", message = "Hola, soy tu asistente de consumos.", isFromUser = false)
    )

    fun addMessage(message: ChatMessage) {
        messages.add(message)
    }

    fun clear() {
        messages.clear()
    }
}
