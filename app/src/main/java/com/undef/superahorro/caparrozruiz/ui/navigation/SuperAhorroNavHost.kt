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
import com.undef.superahorro.caparrozruiz.ui.screens.stats.StatsScreen
import com.undef.superahorro.caparrozruiz.ui.screens.more.MoreScreen

@Composable
fun SuperAhorroNavHost() {
    val navController = rememberNavController() //Gestiona el historial y los cambios de pantallas
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute in mainRoutes //Determina si mostrar la BottomBar o no

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                //Navega al destino asegurando que no se dupliquen pantallas en la pila
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
        //Contenedor que vincula las rutas con sus respectivos Composables
        NavHost(
            navController = navController,
            startDestination = AppRoute.Splash,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.Splash) {
                SplashScreen(
                    onSplashFinished = {
                        //Redirige a LoginScreen y elimina el SplashScreen de la pila
                        navController.navigate(AppRoute.Login) {
                            popUpTo(AppRoute.Splash) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppRoute.Login) {
                LoginScreen(
                    onLoginSuccess = {
                        //En el caso de ir a Home, se borra el LoginScreen de la pila
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
                    onOpenHistory = { navController.navigate(AppRoute.History) }
                )
            }
            composable(AppRoute.History) {
                HistoryScreen(
                    onPurchaseSelected = { purchaseId ->
                        navController.navigate(AppRoute.purchaseDetailRoute(purchaseId))
                    }
                )
            }
            composable(AppRoute.PurchaseDetail) { backStackEntry ->
                val purchaseId = backStackEntry.arguments?.getString("purchaseId").orEmpty()
                PurchaseDetailScreen(
                    purchaseId = purchaseId
                )
            }
            composable(AppRoute.NewPurchase) {
                NewPurchaseScreen(
                    onAddProduct = { navController.navigate(AppRoute.NewProduct) },
                    onSaved = { navController.popBackStack() }
                )
            }
            composable(AppRoute.NewProduct) {
                NewProductScreen(
                    onProductAdded = { navController.popBackStack() }
                )
            }
            composable(AppRoute.Profile) {
                ProfileScreen()
            }
            composable(AppRoute.Settings) {
                SettingsScreen(
                    onLogoutClick = {
                        //Reinicia el flujo de navegacion hacia el Login tras cerrar sesion
                        navController.navigate(AppRoute.Login) {
                            popUpTo(AppRoute.Home) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(AppRoute.More) {
                MoreScreen(
                    onOpenProfile = { navController.navigate(AppRoute.Profile) },
                    onOpenSettings = { navController.navigate(AppRoute.Settings) }
                )
            }
            composable(AppRoute.Stats) {
                StatsScreen()
            }
            composable(AppRoute.Chat) {
                ChatScreen()
            }
        }
    }
}
