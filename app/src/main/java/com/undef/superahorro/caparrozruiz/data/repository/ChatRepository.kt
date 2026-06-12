package com.undef.superahorro.caparrozruiz.data.repository

import android.util.Log
import com.undef.superahorro.caparrozruiz.BuildConfig
import com.undef.superahorro.caparrozruiz.data.dto.GeminiContent
import com.undef.superahorro.caparrozruiz.data.dto.GeminiPart
import com.undef.superahorro.caparrozruiz.data.dto.GeminiRequest
import com.undef.superahorro.caparrozruiz.data.remote.GeminiApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(
    private val apiService: GeminiApiService
) {
    suspend fun sendMessage(userMessage: String, contextData: String = ""): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        Log.d("ChatRepo", "API Key loaded: ${if (apiKey.isNotBlank()) "OK" else "VACIA"}")
        if (apiKey.isBlank()) return@withContext "API key no configurada. Agregala en local.properties y rebuild."

        val systemPrompt = buildString {
            append("Sos un asistente de compras integrado en la app Super Ahorro. Respondé en español de forma clara y breve. IMPORTANTE: los precios están en pesos argentinos ($), no en dólares. ") 
            if (contextData.isNotBlank()) {
                append("Estos son los datos del usuario:\n$contextData\n\n")
            }
            append("Consulta del usuario: $userMessage")
        }

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(text = systemPrompt)
                    )
                )
            )
        )
        val response = runCatching {
            apiService.generateContent(apiKey = apiKey, request = request)
        }.recoverCatching { e ->
            if (e is retrofit2.HttpException && e.code() == 503) {
                Log.d("ChatRepo", "503 temporal, reintentando...")
                kotlinx.coroutines.delay(2000)
                apiService.generateContent(apiKey = apiKey, request = request)
            } else {
                throw e
            }
        }.getOrThrow()
        val text = response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.filter { it.thought != true }
            ?.mapNotNull { it.text }
            ?.joinToString("")
        Log.d("ChatRepo", "Respuesta Gemini: $text")
        text ?: "No pude procesar la consulta."
    }
}
