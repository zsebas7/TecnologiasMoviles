package com.undef.superahorro.caparrozruiz.data.model

data class Purchase(
    val id: Long = 0,
    val market: String,
    val date: String,
    val time: String,
    val total: Double,
    val ticketUri: String = ""
)
