package com.undef.superahorro.caparrozruiz

import android.os.Bundle
import androidx.activity.ComponentActivity //Para trabajar con Jetpack Compose
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.undef.superahorro.caparrozruiz.ui.SuperAhorroApp

class MainActivity : ComponentActivity() {
    //Punto de entrada de la Activity donde se define la UI mediante setContent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()//Habilita el diseño de pantalla completa
        setContent {
            // Se utiliza el patrón Single Activity: SuperAhorroApp gestiona el NavHost
            SuperAhorroApp()
        }
    }
}
