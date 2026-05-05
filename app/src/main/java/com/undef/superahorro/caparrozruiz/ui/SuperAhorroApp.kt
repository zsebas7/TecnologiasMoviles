package com.undef.superahorro.caparrozruiz.ui

import androidx.compose.runtime.Composable
import com.undef.superahorro.caparrozruiz.ui.navigation.SuperAhorroNavHost
import com.undef.superahorro.caparrozruiz.ui.theme.SuperAhorroTheme

@Composable
fun SuperAhorroApp() {
    SuperAhorroTheme {
        SuperAhorroNavHost()
    }
}
