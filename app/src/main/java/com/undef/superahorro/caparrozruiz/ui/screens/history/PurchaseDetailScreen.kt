package com.undef.superahorro.caparrozruiz.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.undef.superahorro.caparrozruiz.ui.components.EditProductCard
import com.undef.superahorro.caparrozruiz.ui.components.EditPurchaseCard
import com.undef.superahorro.caparrozruiz.ui.viewmodel.HistoryViewModel

@Composable
fun PurchaseDetailScreen(
    purchaseId: String,
    viewModel: HistoryViewModel = viewModel()
) {
    val locale = LocalConfiguration.current.locales[0]
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val purchase = uiState.purchases.firstOrNull { it.id == purchaseId }
    val products = uiState.productsByPurchaseId[purchaseId].orEmpty()
    val editingPurchase = uiState.editingPurchase
    val editingProduct = uiState.editingProduct

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
                    Text(text = stringResource(R.string.purchase_detail_date_time, purchase.date, purchase.time))
                    Text(
                        text = stringResource(
                            R.string.purchase_detail_total,
                            "${stringResource(R.string.common_currency_symbol)} ${"%.2f".format(locale, purchase.total)}"
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { viewModel.startEditPurchase(purchase.id) }) {
                            Text(text = stringResource(R.string.purchase_detail_edit_button))
                        }
                        OutlinedButton(onClick = { viewModel.deletePurchase(purchase.id) }) {
                            Text(text = stringResource(R.string.purchase_detail_delete_button))
                        }
                    }
                }
            }
        }
        if (editingPurchase != null && editingPurchase.id == purchaseId) {
            EditPurchaseCard(
                market = editingPurchase.market,
                date = editingPurchase.date,
                time = editingPurchase.time,
                total = editingPurchase.total,
                onDismiss = viewModel::clearEdits,
                onSave = { market, date, time, total ->
                    viewModel.updatePurchase(purchaseId, market, date, time, total)
                }
            )
        }
        Text(text = stringResource(R.string.purchase_detail_products_title), style = MaterialTheme.typography.titleMedium)
        products.forEach { product ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = product.name, style = MaterialTheme.typography.titleSmall)
                    if (product.code.isNotBlank()) {
                        Text(text = stringResource(R.string.purchase_detail_product_code, product.code))
                    }
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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { viewModel.startEditProduct(purchaseId, product.id) }) {
                            Text(text = stringResource(R.string.purchase_detail_edit_product_button))
                        }
                        OutlinedButton(onClick = { viewModel.deleteProduct(purchaseId, product.id) }) {
                            Text(text = stringResource(R.string.purchase_detail_delete_product_button))
                        }
                    }
                }
            }
            if (editingProduct != null && editingProduct.id == product.id) {
                EditProductCard(
                    code = editingProduct.code,
                    name = editingProduct.name,
                    description = editingProduct.description,
                    quantity = editingProduct.quantity,
                    price = editingProduct.price,
                    onDismiss = viewModel::clearEdits,
                    onSave = { code, name, description, quantity, price ->
                        viewModel.updateProduct(
                            purchaseId = purchaseId,
                            productId = editingProduct.id,
                            code = code,
                            name = name,
                            description = description,
                            quantity = quantity,
                            price = price
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
