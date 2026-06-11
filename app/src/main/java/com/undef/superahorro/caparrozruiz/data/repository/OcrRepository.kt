package com.undef.superahorro.caparrozruiz.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import com.undef.superahorro.caparrozruiz.BuildConfig
import com.undef.superahorro.caparrozruiz.data.model.ParsedTicket
import com.undef.superahorro.caparrozruiz.data.remote.OcrApiService
import com.undef.superahorro.caparrozruiz.util.TicketParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OcrRepository(
    private val apiService: OcrApiService,
    private val contentResolver: ContentResolver
) {
    suspend fun analyzeTicket(uriString: String): ParsedTicket = withContext(Dispatchers.IO) {
        if (BuildConfig.OCR_SPACE_API_KEY.isBlank()) {
            throw IllegalStateException("OCR_SPACE_API_KEY no configurada en local.properties")
        }

        val bytes = contentResolver.openInputStream(Uri.parse(uriString))?.use { it.readBytes() }
            ?: throw Exception("No se pudo leer la imagen del ticket")

        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        val base64Image = "data:image/jpeg;base64,$base64"

        val response = apiService.parseImage(
            apiKey = BuildConfig.OCR_SPACE_API_KEY,
            base64Image = base64Image,
            language = "spa",
            ocrEngine = 2,
            isOverlayRequired = false
        )

        if (response.isErroredOnProcessing) {
            val msg = response.parsedResults.firstOrNull()?.errorMessage
            throw Exception(msg?.ifBlank { "Error al procesar la imagen" } ?: "Error al procesar la imagen")
        }

        val rawText = response.parsedResults.firstOrNull()?.parsedText.orEmpty()
        TicketParser.parse(rawText)
    }
}
