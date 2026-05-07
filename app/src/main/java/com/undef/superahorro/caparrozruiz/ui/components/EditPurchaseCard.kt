package com.undef.superahorro.caparrozruiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.undef.superahorro.caparrozruiz.R

@Composable
fun EditPurchaseCard(
    market: String,
    date: String,
    time: String,
    total: Double,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Double) -> Unit
) {
    var editedMarket by remember { mutableStateOf(market) }
    var editedDate by remember { mutableStateOf(date) }
    var editedTime by remember { mutableStateOf(time) }
    var editedTotal by remember { mutableStateOf(total.toString()) }

    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = stringResource(R.string.history_edit_title), style = MaterialTheme.typography.titleMedium)
            AppTextField(
                value = editedMarket,
                onValueChange = { editedMarket = it },
                label = stringResource(R.string.purchase_new_market_label)
            )
            AppTextField(
                value = editedDate,
                onValueChange = { editedDate = it },
                label = stringResource(R.string.purchase_new_date_label)
            )
            AppTextField(
                value = editedTime,
                onValueChange = { editedTime = it },
                label = stringResource(R.string.purchase_new_time_label)
            )
            AppTextField(
                value = editedTotal,
                onValueChange = { editedTotal = it },
                label = stringResource(R.string.purchase_new_total_label),
                keyboardType = KeyboardType.Number
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.common_cancel))
                }
                OutlinedButton(
                    onClick = {
                        val totalValue = editedTotal.toDoubleOrNull() ?: 0.0
                        onSave(editedMarket, editedDate, editedTime, totalValue)
                    }
                ) {
                    Text(text = stringResource(R.string.common_save))
                }
            }
        }
    }
}
