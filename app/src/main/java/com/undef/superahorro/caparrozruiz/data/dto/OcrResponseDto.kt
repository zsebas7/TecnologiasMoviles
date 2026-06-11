package com.undef.superahorro.caparrozruiz.data.dto

import com.google.gson.annotations.SerializedName

data class OcrResponseDto(
    @SerializedName("ParsedResults") val parsedResults: List<OcrParsedResultDto> = emptyList(),
    @SerializedName("IsErroredOnProcessing") val isErroredOnProcessing: Boolean = false
)

data class OcrParsedResultDto(
    @SerializedName("ParsedText") val parsedText: String = "",
    @SerializedName("ErrorMessage") val errorMessage: String = ""
)
