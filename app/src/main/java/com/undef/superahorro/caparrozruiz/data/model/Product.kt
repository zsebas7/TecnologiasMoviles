package com.undef.superahorro.caparrozruiz.data.model

data class Product(
    val id: Long = 0,
    val code: String,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
)
