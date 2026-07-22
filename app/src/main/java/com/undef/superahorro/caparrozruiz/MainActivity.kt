package com.undef.superahorro.caparrozruiz

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity //Para trabajar con Jetpack Compose
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.undef.superahorro.caparrozruiz.ui.SuperAhorroApp

class MainActivity : ComponentActivity() {
    //Punto de entrada de la Activity donde se define la UI mediante setContent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()//Habilita el diseño de pantalla completa
        requestNotificationPermissionIfNeeded()
        setContent {
            // Se utiliza el patrón Single Activity: SuperAhorroApp gestiona el NavHost
            SuperAhorroApp()
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
    }
}
