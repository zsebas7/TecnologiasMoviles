package com.undef.superahorro.caparrozruiz.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.undef.superahorro.caparrozruiz.R

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(route = AppRoute.Home, icon = Icons.Default.Home, labelRes = R.string.nav_home),
    BottomNavItem(route = AppRoute.Stats, icon = Icons.Default.BarChart, labelRes = R.string.nav_stats),
    BottomNavItem(route = AppRoute.NewPurchase, icon = Icons.Default.AddShoppingCart, labelRes = R.string.nav_new_purchase),
    BottomNavItem(route = AppRoute.More, icon = Icons.Default.MoreHoriz, labelRes = R.string.nav_more),
    BottomNavItem(route = AppRoute.Chat, icon = Icons.AutoMirrored.Filled.Chat, labelRes = R.string.nav_chat)

)
