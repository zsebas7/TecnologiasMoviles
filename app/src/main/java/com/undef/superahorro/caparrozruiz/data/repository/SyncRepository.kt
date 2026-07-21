package com.undef.superahorro.caparrozruiz.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.undef.superahorro.caparrozruiz.data.dto.SyncPurchaseItemDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SyncRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun syncPurchases(purchases: List<SyncPurchaseItemDto>): String = withContext(Dispatchers.IO) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: return@withContext "Error: usuario no autenticado"
        var count = 0
        for (purchase in purchases) {
            val data = hashMapOf(
                "market" to purchase.market,
                "date" to purchase.date,
                "time" to purchase.time,
                "total" to purchase.total,
                "products" to purchase.products.map { product ->
                    hashMapOf(
                        "name" to product.name,
                        "quantity" to product.quantity,
                        "price" to product.price
                    )
                }
            )
            val docId = purchase.id.toString()
            firestore.collection("users").document(uid)
                .collection("purchases").document(docId).set(data).await()
            count++
        }
        "Sync ok: $count compras sincronizadas a la nube"
    }
}
