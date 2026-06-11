package com.undef.superahorro.caparrozruiz.util

import com.undef.superahorro.caparrozruiz.data.model.ParsedTicket

object TicketParser {

    fun parse(text: String): ParsedTicket {
        val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() }
        return ParsedTicket(
            market = extractMarket(lines),
            date = extractDate(text),
            time = extractTime(text),
            total = extractTotal(text)
        )
    }

    // Primera línea que tenga solo letras y espacios (probable nombre del comercio)
    private fun extractMarket(lines: List<String>): String =
        lines.firstOrNull { line ->
            line.length in 3..50 &&
            line.any { it.isLetter() } &&
            line.none { it.isDigit() }
        }.orEmpty()

    // DD/MM/YYYY o DD-MM-YYYY → convierte a YYYY-MM-DD
    private fun extractDate(text: String): String {
        val ddmmyyyy = Regex("""(\d{2})[/\-](\d{2})[/\-](\d{4})""")
        ddmmyyyy.find(text)?.let { m ->
            return "${m.groupValues[3]}-${m.groupValues[2]}-${m.groupValues[1]}"
        }
        val yyyymmdd = Regex("""(\d{4})-(\d{2})-(\d{2})""")
        yyyymmdd.find(text)?.let { m -> return m.value }
        return ""
    }

    // HH:MM (primera ocurrencia)
    private fun extractTime(text: String): String {
        val pattern = Regex("""(\d{2}):(\d{2})""")
        return pattern.find(text)?.let { "${it.groupValues[1]}:${it.groupValues[2]}" } ?: ""
    }

    // Busca "TOTAL", "IMPORTE", "A PAGAR" seguido de un número
    // Soporta formato argentino: 1.234,56 → 1234.56
    private fun extractTotal(text: String): String {
        val patterns = listOf(
            Regex("""TOTAL\s*A\s*PAGAR\s*[\$:]?\s*([\d.,]+)""", RegexOption.IGNORE_CASE),
            Regex("""TOTAL\s*[\$:]?\s*([\d.,]+)""", RegexOption.IGNORE_CASE),
            Regex("""IMPORTE\s*[\$:]?\s*([\d.,]+)""", RegexOption.IGNORE_CASE),
            Regex("""A\s*PAGAR\s*[\$:]?\s*([\d.,]+)""", RegexOption.IGNORE_CASE)
        )
        for (pattern in patterns) {
            val raw = pattern.find(text)?.groupValues?.get(1) ?: continue
            val normalized = when {
                raw.contains(",") && raw.contains(".") ->
                    raw.replace(".", "").replace(",", ".")  // 1.234,56 → 1234.56
                raw.contains(",") ->
                    raw.replace(",", ".")                   // 1234,56 → 1234.56
                else -> raw
            }
            val value = normalized.toDoubleOrNull() ?: continue
            return "%.2f".format(value)
        }
        return ""
    }
}
