package com.undef.superahorro.caparrozruiz.ui.state

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val monthlySummaryEnabled: Boolean = true,
    val isSyncing: Boolean = false,
    val syncMessage: String = "",
    val syncError: String = ""
)
