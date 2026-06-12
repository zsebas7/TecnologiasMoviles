package com.undef.superahorro.caparrozruiz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases")
//representa una fila en la tabla de  purchases
//purchaseEntity no llega a la UI, se convierte en Purchase abtes
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)//se genera automaticamente la clave primaria
    val id: Long = 0,
    val market: String,
    val date: String,
    val time: String,
    val total: Double,
    @ColumnInfo(name = "ticket_uri") //para guardar la direccion de la imagen
    val ticketUri: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
