package com.undef.superahorro.caparrozruiz.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.undef.superahorro.caparrozruiz.core.AppContainer
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.ui.state.CategorySlice
import com.undef.superahorro.caparrozruiz.ui.state.ChartEntry
import com.undef.superahorro.caparrozruiz.ui.state.StatsMode
import com.undef.superahorro.caparrozruiz.ui.state.StatsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatsViewModel : ViewModel() {
    private val repository = AppContainer.walletRepository
    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observePurchases().collectLatest { purchases ->
                _uiState.value = _uiState.value.copy(
                    entries = buildEntries(purchases, _uiState.value.mode),
                    categories = buildCategories(purchases)
                )
            }
        }
    }

    fun setMode(mode: StatsMode) {
        if (_uiState.value.mode == mode) return
        viewModelScope.launch {
            repository.observePurchases().collectLatest { purchases ->
                _uiState.value = StatsUiState(
                    mode = mode,
                    entries = buildEntries(purchases, mode),
                    categories = buildCategories(purchases)
                )
                return@collectLatest
            }
        )
    }

    private fun buildEntries(purchases: List<Purchase>, mode: StatsMode): List<ChartEntry> {
        if (purchases.isEmpty()) return emptyList()

        val totals = when (mode) {
            StatsMode.Daily -> purchases.groupBy { it.date }
                .mapValues { (_, list) -> list.sumOf { it.total } }
            StatsMode.Monthly -> purchases.groupBy { it.date.take(7) }
                .mapValues { (_, list) -> list.sumOf { it.total } }
        }

        val sortedKeys = totals.keys.sorted()
        val limitedKeys = if (sortedKeys.size > 6) sortedKeys.takeLast(6) else sortedKeys

        return limitedKeys.map { key ->
            val label = if (mode == StatsMode.Daily && key.length >= 10) {
                key.substring(5, 10)
            } else {
                key
            }
            ChartEntry(label = label, value = totals[key] ?: 0.0)
        }
    }

    private fun buildCategories(purchases: List<Purchase>): List<CategorySlice> {
        if (purchases.isEmpty()) return emptyList()

        val totals = purchases.groupBy { it.market }
            .mapValues { (_, list) -> list.sumOf { it.total } }

        val palette = listOf(
            Color(0xFF2A9D8F),
            Color(0xFFF4A261),
            Color(0xFFE76F51),
            Color(0xFF6D597A),
            Color(0xFF457B9D)
        )

        return totals.entries.sortedByDescending { it.value }.mapIndexed { index, entry ->
            CategorySlice(
                label = entry.key,
                value = entry.value,
                color = palette[index % palette.size]
            )
        }
    }

}
