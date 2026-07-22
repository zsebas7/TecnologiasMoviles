package com.undef.superahorro.caparrozruiz.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.undef.superahorro.caparrozruiz.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "purchases_channel"
    }

    fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Compras guardadas",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun showPurchaseSaved(market: String, total: Double) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Compra guardada")
            .setContentText("$market · $${"%.2f".format(total)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
