package com.undef.superahorro.caparrozruiz.ui.screens.purchase

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.components.AppTextField
import com.undef.superahorro.caparrozruiz.ui.components.PrimaryButton
import com.undef.superahorro.caparrozruiz.ui.viewmodel.NewPurchaseViewModel
import android.content.Intent
import androidx.compose.ui.platform.LocalContext

@Composable
fun NewPurchaseScreen(
    onAddProduct: () -> Unit,
    onSaved: () -> Unit,
    viewModel: NewPurchaseViewModel = viewModel()
) {
    val uiState by viewModel.purchaseState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.purchase_new_title), style = MaterialTheme.typography.headlineMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    value = uiState.date,
                    onValueChange = viewModel::onDateChanged,
                    label = stringResource(R.string.purchase_new_date_label)
                )
                AppTextField(
                    value = uiState.time,
                    onValueChange = viewModel::onTimeChanged,
                    label = stringResource(R.string.purchase_new_time_label)
                )
                AppTextField(
                    value = uiState.market,
                    onValueChange = viewModel::onMarketChanged,
                    label = stringResource(R.string.purchase_new_market_label)
                )
                AppTextField(
                    value = uiState.total,
                    onValueChange = {},
                    label = stringResource(R.string.purchase_new_total_label),
                    keyboardType = KeyboardType.Number,
                    readOnly = true
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.purchase_new_ticket_title), style = MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
                                context.startActivity(intent)
                                viewModel.setTicketStatus("Galería")
                            }
                        ) {
                            Text(text = stringResource(R.string.purchase_new_ticket_gallery))
                        }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent("android.media.action.IMAGE_CAPTURE")
                                context.startActivity(intent)
                                viewModel.setTicketStatus("Cámara")
                            }
                        ) {
                            Text(text = stringResource(R.string.purchase_new_ticket_camera))
                        }
                    }
                    if (uiState.ticketStatus.isNotBlank()) {
                        Text(
                            text = stringResource(
                                R.string.purchase_new_ticket_status,
                                uiState.ticketStatus
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.purchase_new_products_title), style = MaterialTheme.typography.titleMedium)
            Button(onClick = onAddProduct) {
                Text(text = stringResource(R.string.purchase_new_add_product_button))
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.products, key = { it.id }) { product ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = product.name, style = MaterialTheme.typography.titleSmall)
                        Text(text = product.description, style = MaterialTheme.typography.bodySmall)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = stringResource(R.string.purchase_new_product_quantity, product.quantity))
                            Text(text = stringResource(R.string.purchase_new_product_price, product.price))
                        }
                        Button(onClick = { viewModel.removeDraftProduct(product.id) }) {
                            Text(text = stringResource(R.string.purchase_new_remove_product_button))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        PrimaryButton(
            text = stringResource(R.string.purchase_new_save_button),
            loading = uiState.isSaving,
            onClick = { viewModel.savePurchase(onSaved) }
        )
    }
}
