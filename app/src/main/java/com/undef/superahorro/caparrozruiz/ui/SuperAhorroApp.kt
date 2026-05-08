package com.undef.superahorro.caparrozruiz.ui

import androidx.compose.runtime.Composable
import com.undef.superahorro.caparrozruiz.ui.navigation.SuperAhorroNavHost
import com.undef.superahorro.caparrozruiz.ui.theme.SuperAhorroTheme

@Composable
fun SuperAhorroApp() {
    SuperAhorroTheme(dynamicColor = false) { // Para que la app use el tema de la UI
        SuperAhorroNavHost() //NavHost es el encargado de decir que pantalla se muestra
    }
}
