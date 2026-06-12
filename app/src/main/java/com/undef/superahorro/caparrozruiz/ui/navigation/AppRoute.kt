package com.undef.superahorro.caparrozruiz.ui.navigation

object AppRoute {
    const val Splash = "splash"
    const val Login = "auth/login"
    const val Register = "auth/register"
    const val ForgotPassword = "auth/forgot"
    const val Home = "main/home"
    const val Profile = "main/profile"
    const val Settings = "main/settings"
    const val More = "main/more"
    const val Promotions = "main/promotions"
    const val Chat = "main/chat"
    const val Stats = "main/stats"
    const val History = "main/history"
    const val PurchaseDetail = "main/purchase/{purchaseId}"
    const val NewPurchase = "main/purchase/new"
    const val NewProduct = "main/product/new"

    fun purchaseDetailRoute(purchaseId: String): String = "main/purchase/$purchaseId"
}

val mainRoutes = setOf(
    AppRoute.Home,
    AppRoute.NewPurchase,
    AppRoute.More,
    AppRoute.Chat,
    AppRoute.Stats
)
