package com.undef.superahorro.caparrozruiz.data.repository

import com.undef.superahorro.caparrozruiz.data.model.ChatMessage
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.data.model.User

class FakeWalletRepository {

    fun getCurrentUser(): User {
        return User(
            name = "Martin Perez",
            email = "martin.perez@email.com",
            city = "Cordoba",
            monthlyBudget = 265000.0
        )
    }

    fun getLatestPurchases(): List<Purchase> {
        return listOf(
            Purchase(id = "P001", market = "Carrefour", date = "2026-05-01", total = 38650.0),
            Purchase(id = "P002", market = "Disco", date = "2026-04-29", total = 23400.0),
            Purchase(id = "P003", market = "Coto", date = "2026-04-27", total = 41200.0),
            Purchase(id = "P004", market = "Dia", date = "2026-04-24", total = 15780.0)
        )
    }

    fun getInitialChatMessages(): List<ChatMessage> {
        return listOf(
            ChatMessage(id = "m1", message = "Hola, soy tu asistente de consumos.", isFromUser = false),
            ChatMessage(id = "m2", message = "Podes preguntar por precios o historial.", isFromUser = false)
        )
    }
}
