package com.undef.superahorro.caparrozruiz.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.undef.superahorro.caparrozruiz.ui.viewmodel.HistoryViewModel

@Composable
fun PurchaseDetailScreen(
    purchaseId: String,
    onBack: () -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    val locale = LocalConfiguration.current.locales[0]
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val purchase = uiState.purchases.firstOrNull { it.id == purchaseId }
    val products = viewModel.productsForPurchase(purchaseId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.purchase_detail_title), style = MaterialTheme.typography.headlineMedium)
        if (purchase != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = purchase.market, style = MaterialTheme.typography.titleMedium)
                    Text(text = purchase.date, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = stringResource(
                            R.string.purchase_detail_total,
                            "${stringResource(R.string.common_currency_symbol)} ${"%.2f".format(locale, purchase.total)}"
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        Text(text = stringResource(R.string.purchase_detail_products_title), style = MaterialTheme.typography.titleMedium)
        products.forEach { product ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = product.name, style = MaterialTheme.typography.titleSmall)
                    Text(text = product.description, style = MaterialTheme.typography.bodySmall)
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(R.string.purchase_detail_quantity, product.quantity))
                        Text(
                            text = stringResource(
                                R.string.purchase_detail_price,
                                "${stringResource(R.string.common_currency_symbol)} ${"%.2f".format(locale, product.price)}"
                            )
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack) {
            Text(text = stringResource(R.string.purchase_detail_back_button))
        }
    }
}
