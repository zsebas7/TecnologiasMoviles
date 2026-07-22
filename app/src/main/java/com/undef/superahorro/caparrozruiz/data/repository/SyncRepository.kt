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
        val purchasesRef = firestore.collection("users").document(uid).collection("purchases")
        //borra lo que habia antes
        val existing = purchasesRef.get().await()
        for (doc in existing.documents) {
            doc.reference.delete().await()
        }
        var count = 0
        //sube el estado actual
        //la accion de borrar y volver a subir esta para que en Firestore siempre esté lo que ve el usuario en el celular, sin duplicados ni compras borradas
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
            purchasesRef.document(docId).set(data).await()
            count++
        }
        "Sync ok: $count compras sincronizadas a la nube"
    }
}
