package com.undef.superahorro.caparrozruiz.ui.navigation

object AppRoute {
    const val Splash = "splash"
    const val Login = "auth/login"
    const val Register = "auth/register"
    const val ForgotPassword = "auth/forgot"
    const val Home = "main/home"
    const val Profile = "main/profile"
    const val Settings = "main/settings"
    const val Chat = "main/chat"
}

val mainRoutes = setOf(
    AppRoute.Home,
    AppRoute.Profile,
    AppRoute.Settings,
    AppRoute.Chat
)
