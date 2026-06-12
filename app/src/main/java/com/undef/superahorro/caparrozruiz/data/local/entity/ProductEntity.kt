package com.undef.superahorro.caparrozruiz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchase_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["purchase_id"])]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "purchase_id")
    val purchaseId: Long,
    val code: String,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
)
