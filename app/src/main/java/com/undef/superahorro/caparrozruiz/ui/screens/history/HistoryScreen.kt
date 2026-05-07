package com.undef.superahorro.caparrozruiz.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onPurchaseSelected: (String) -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var deleteTargetId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.history_title), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.history_subtitle), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        val editingPurchase = uiState.editingPurchase
        if (deleteTargetId != null) {
            AlertDialog(
                onDismissRequest = { deleteTargetId = null },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteTargetId?.let(viewModel::deletePurchase)
                            deleteTargetId = null
                        }
                    ) {
                        Text(text = stringResource(R.string.common_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteTargetId = null }) {
                        Text(text = stringResource(R.string.common_cancel))
                    }
                },
                title = { Text(text = stringResource(R.string.history_confirm_delete_title)) },
                text = { Text(text = stringResource(R.string.history_confirm_delete_message)) }
            )
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(uiState.purchases, key = { it.id }) { purchase ->
                PurchaseItem(
                    purchase = purchase,
                    currencyLabel = stringResource(R.string.common_currency_symbol),
                    modifier = Modifier.clickable { onPurchaseSelected(purchase.id) }
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    OutlinedButton(onClick = { onPurchaseSelected(purchase.id) }) {
                        Text(text = stringResource(R.string.history_edit_button))
                    }
                    OutlinedButton(onClick = { deleteTargetId = purchase.id }) {
                        Text(text = stringResource(R.string.history_delete_button))
                    }
                }
            }
        }
    }
}
