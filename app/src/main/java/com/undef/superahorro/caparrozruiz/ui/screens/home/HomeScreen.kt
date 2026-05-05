package com.undef.superahorro.caparrozruiz.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.components.PurchaseItem
import com.undef.superahorro.caparrozruiz.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onOpenChat: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locale = LocalConfiguration.current.locales[0]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.home_welcome, uiState.userName),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = stringResource(R.string.home_budget_title), style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            R.string.home_budget_value,
                            "${stringResource(R.string.common_currency_symbol)} ${"%.2f".format(locale, uiState.monthlyBudget)}"
                        ),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.home_latest_purchases_title), style = MaterialTheme.typography.titleLarge)
                Button(onClick = onOpenChat) {
                    Text(text = stringResource(R.string.home_open_chat_button))
                }
            }
        }
        items(uiState.purchases, key = { it.id }) { purchase ->
            PurchaseItem(
                purchase = purchase,
                currencyLabel = stringResource(R.string.common_currency_symbol)
            )
        }
    }
}
