package com.undef.superahorro.caparrozruiz.ui.screens.history

import android.content.Intent
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val id = purchaseId.toLongOrNull() ?: -1L
    val context = LocalContext.current
    val locale = LocalConfiguration.current.locales[0]
    val currencySymbol = stringResource(R.string.common_currency_symbol)
    val productsTitle = stringResource(R.string.purchase_detail_products_title)
    val shareChooserTitle = stringResource(R.string.purchase_detail_share_chooser)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val purchase = uiState.purchases.firstOrNull { it.id == id }
    val products = uiState.productsByPurchaseId[id].orEmpty()
    val editingPurchase = uiState.editingPurchase
    val editingProduct = uiState.editingProduct
    var deletePurchaseId by remember { mutableStateOf<Long?>(null) }
    var deleteProductId by remember { mutableStateOf<Long?>(null) }

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
                        OutlinedButton(onClick = { deletePurchaseId = purchase.id }) {
                            Text(text = stringResource(R.string.purchase_detail_delete_button))
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            val shareText = buildString {
                                appendLine("${purchase.market} - ${purchase.date} ${purchase.time}")
                                appendLine("Total: $currencySymbol ${"%.2f".format(locale, purchase.total)}")
                                if (products.isNotEmpty()) {
                                    appendLine()
                                    appendLine(productsTitle)
                                    products.forEach { product ->
                                        appendLine("- ${product.name} x${product.quantity}")
                                    }
                                }
                            }
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            val chooser = Intent.createChooser(sendIntent, shareChooserTitle)
                            context.startActivity(chooser)
                        }
                    ) {
                        Text(text = stringResource(R.string.purchase_detail_share_button))
                    }
                }
            }
        }
        if (deletePurchaseId != null) {
            AlertDialog(
                onDismissRequest = { deletePurchaseId = null },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deletePurchaseId?.let { viewModel.deletePurchase(it) }
                            deletePurchaseId = null
                        }
                    ) {
                        Text(text = stringResource(R.string.common_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deletePurchaseId = null }) {
                        Text(text = stringResource(R.string.common_cancel))
                    }
                },
                title = { Text(text = stringResource(R.string.purchase_detail_delete_title)) },
                text = { Text(text = stringResource(R.string.purchase_detail_delete_message)) }
            )
        }
        if (editingPurchase != null && editingPurchase.id == id) {
            EditPurchaseCard(
                market = editingPurchase.market,
                date = editingPurchase.date,
                time = editingPurchase.time,
                total = editingPurchase.total,
                onDismiss = viewModel::clearEdits,
                onSave = { market, date, time, total ->
                    viewModel.updatePurchase(id, market, date, time, total)
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
                        OutlinedButton(onClick = { viewModel.startEditProduct(id, product.id) }) {
                            Text(text = stringResource(R.string.purchase_detail_edit_product_button))
                        }
                        OutlinedButton(onClick = { deleteProductId = product.id }) {
                            Text(text = stringResource(R.string.purchase_detail_delete_product_button))
                        }
                    }
                }
            }
            if (editingProduct != null && editingProduct.id == product.id) {
                EditProductCard(
                    name = editingProduct.name,
                    description = editingProduct.description,
                    quantity = editingProduct.quantity,
                    price = editingProduct.price,
                    onDismiss = viewModel::clearEdits,
                    onSave = { name, description, quantity, price ->
                        viewModel.updateProduct(
                            purchaseId = id,
                            productId = editingProduct.id,
                            code = editingProduct.code,
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

    if (deleteProductId != null) {
        AlertDialog(
            onDismissRequest = { deleteProductId = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteProductId?.let { viewModel.deleteProduct(id, it) }
                        deleteProductId = null
                    }
                ) {
                    Text(text = stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteProductId = null }) {
                    Text(text = stringResource(R.string.common_cancel))
                }
            },
            title = { Text(text = stringResource(R.string.purchase_detail_delete_product_title)) },
            text = { Text(text = stringResource(R.string.purchase_detail_delete_product_message)) }
        )
    }
}
