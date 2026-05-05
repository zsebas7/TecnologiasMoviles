package com.undef.superahorro.caparrozruiz.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.components.PurchaseItem
import com.undef.superahorro.caparrozruiz.ui.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onPurchaseSelected: (String) -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.history_title), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.history_subtitle), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(uiState.purchases, key = { it.id }) { purchase ->
                PurchaseItem(
                    purchase = purchase,
                    currencyLabel = stringResource(R.string.common_currency_symbol),
                    modifier = Modifier.clickable { onPurchaseSelected(purchase.id) }
                )
            }
        }
        Button(onClick = onBack) {
            Text(text = stringResource(R.string.history_back_button))
        }
    }
}
