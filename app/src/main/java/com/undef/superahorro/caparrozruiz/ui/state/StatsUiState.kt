package com.undef.superahorro.caparrozruiz.ui.state

data class StatsUiState(
    val mode: StatsMode = StatsMode.Daily,
    val entries: List<ChartEntry> = emptyList(),
    val categories: List<CategorySlice> = emptyList()
)
