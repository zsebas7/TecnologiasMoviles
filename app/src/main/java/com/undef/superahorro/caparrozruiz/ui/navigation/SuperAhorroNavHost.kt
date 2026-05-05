package com.undef.superahorro.caparrozruiz.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.undef.superahorro.caparrozruiz.ui.screens.auth.ForgotPasswordScreen
import com.undef.superahorro.caparrozruiz.ui.screens.auth.LoginScreen
import com.undef.superahorro.caparrozruiz.ui.screens.auth.RegisterScreen
import com.undef.superahorro.caparrozruiz.ui.screens.chat.ChatScreen
import com.undef.superahorro.caparrozruiz.ui.screens.history.HistoryScreen
import com.undef.superahorro.caparrozruiz.ui.screens.history.PurchaseDetailScreen
import com.undef.superahorro.caparrozruiz.ui.screens.home.HomeScreen
import com.undef.superahorro.caparrozruiz.ui.screens.purchase.NewProductScreen
import com.undef.superahorro.caparrozruiz.ui.screens.purchase.NewPurchaseScreen
import com.undef.superahorro.caparrozruiz.ui.screens.profile.ProfileScreen
import com.undef.superahorro.caparrozruiz.ui.screens.settings.SettingsScreen
import com.undef.superahorro.caparrozruiz.ui.screens.splash.SplashScreen

@Composable
fun SuperAhorroNavHost() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute in mainRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = item.icon, contentDescription = null) },
                            label = { Text(text = stringResource(item.labelRes)) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Splash,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.Splash) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(AppRoute.Login) {
                            popUpTo(AppRoute.Splash) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppRoute.Login) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppRoute.Home) {
                            popUpTo(AppRoute.Login) { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate(AppRoute.Register) },
                    onForgotPasswordClick = { navController.navigate(AppRoute.ForgotPassword) }
                )
            }
            composable(AppRoute.Register) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(AppRoute.Home) {
                            popUpTo(AppRoute.Login) { inclusive = true }
                        }
                    },
                    onBackToLogin = { navController.popBackStack() }
                )
            }
            composable(AppRoute.ForgotPassword) {
                ForgotPasswordScreen(onBackToLogin = { navController.popBackStack() })
            }
            composable(AppRoute.Home) {
                HomeScreen(
                    onOpenChat = { navController.navigate(AppRoute.Chat) },
                    onOpenHistory = { navController.navigate(AppRoute.History) },
                    onOpenNewPurchase = { navController.navigate(AppRoute.NewPurchase) }
                )
            }
            composable(AppRoute.History) {
                HistoryScreen(
                    onBack = { navController.popBackStack() },
                    onPurchaseSelected = { purchaseId ->
                        navController.navigate(AppRoute.purchaseDetailRoute(purchaseId))
                    }
                )
            }
            composable(AppRoute.PurchaseDetail) { backStackEntry ->
                val purchaseId = backStackEntry.arguments?.getString("purchaseId").orEmpty()
                PurchaseDetailScreen(
                    purchaseId = purchaseId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppRoute.NewPurchase) {
                NewPurchaseScreen(
                    onBack = { navController.popBackStack() },
                    onAddProduct = { navController.navigate(AppRoute.NewProduct) },
                    onSaved = { navController.popBackStack() }
                )
            }
            composable(AppRoute.NewProduct) {
                NewProductScreen(
                    onBack = { navController.popBackStack() },
                    onProductAdded = { navController.popBackStack() }
                )
            }
            composable(AppRoute.Profile) {
                ProfileScreen()
            }
            composable(AppRoute.Settings) {
                SettingsScreen(
                    onLogoutClick = {
                        navController.navigate(AppRoute.Login) {
                            popUpTo(AppRoute.Home) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(AppRoute.Chat) {
                ChatScreen()
            }
        }
    }
}
