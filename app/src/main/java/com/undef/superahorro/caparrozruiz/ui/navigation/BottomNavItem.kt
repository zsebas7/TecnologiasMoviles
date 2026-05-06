package com.undef.superahorro.caparrozruiz.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.undef.superahorro.caparrozruiz.R

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(route = AppRoute.Home, icon = Icons.Default.Home, labelRes = R.string.nav_home),
    BottomNavItem(route = AppRoute.Profile, icon = Icons.Default.Person, labelRes = R.string.nav_profile),
    BottomNavItem(route = AppRoute.NewPurchase, icon = Icons.Default.AddShoppingCart, labelRes = R.string.nav_new_purchase),
    BottomNavItem(route = AppRoute.Settings, icon = Icons.Default.Settings, labelRes = R.string.nav_settings),
    BottomNavItem(route = AppRoute.Chat, icon = Icons.AutoMirrored.Filled.Chat, labelRes = R.string.nav_chat)
)
