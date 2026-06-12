package com.undef.superahorro.caparrozruiz.ui.screens.history

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.components.ConfirmDialog
import com.undef.superahorro.caparrozruiz.ui.components.EditProductCard
import com.undef.superahorro.caparrozruiz.ui.components.EditPurchaseCard
import com.undef.superahorro.caparrozruiz.ui.viewmodel.HistoryViewModel
import com.undef.superahorro.caparrozruiz.util.toCurrencyString

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

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.purchase_detail_title), style = MaterialTheme.typography.headlineMedium)
        if (purchase != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = purchase.market, style = MaterialTheme.typography.titleMedium)
                            Text(text = stringResource(R.string.purchase_detail_date_time, purchase.date, purchase.time))
                        }
                        Box {
                            var expanded by remember { mutableStateOf(false) }
                            IconButton(onClick = { expanded = true }) {
                                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.purchase_detail_edit_button)) },
                                    onClick = {
                                        expanded = false
                                        viewModel.startEditPurchase(purchase.id)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.purchase_detail_delete_button)) },
                                    onClick = {
                                        expanded = false
                                        deletePurchaseId = purchase.id
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.purchase_detail_share_button)) },
                                    onClick = {
                                        expanded = false
                                        val shareText = buildString {
                                            appendLine("${purchase.market} - ${purchase.date} ${purchase.time}")
                                            appendLine("Total: $currencySymbol ${purchase.total.toCurrencyString(locale)}")
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
                                )
                            }
                        }
                    }
                    Text(
                        text = stringResource(
                            R.string.purchase_detail_total,
                            "${stringResource(R.string.common_currency_symbol)} ${purchase.total.toCurrencyString(locale)}"
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            if (!purchase.ticketUri.isNullOrBlank()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = stringResource(R.string.purchase_detail_ticket_label), style = MaterialTheme.typography.titleSmall)
                        AsyncImage(
                            model = purchase.ticketUri,
                            contentDescription = stringResource(R.string.purchase_detail_ticket_label),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        if (deletePurchaseId != null) {
            ConfirmDialog(
                title = stringResource(R.string.purchase_detail_delete_title),
                message = stringResource(R.string.purchase_detail_delete_message),
                onConfirm = {
                    deletePurchaseId?.let { viewModel.deletePurchase(it) }
                    deletePurchaseId = null
                },
                onDismiss = { deletePurchaseId = null }
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
                                "${stringResource(R.string.common_currency_symbol)} ${product.price.toCurrencyString(locale)}"
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
        ConfirmDialog(
            title = stringResource(R.string.purchase_detail_delete_product_title),
            message = stringResource(R.string.purchase_detail_delete_product_message),
            onConfirm = {
                deleteProductId?.let { viewModel.deleteProduct(id, it) }
                deleteProductId = null
            },
            onDismiss = { deleteProductId = null }
        )
    }
}
