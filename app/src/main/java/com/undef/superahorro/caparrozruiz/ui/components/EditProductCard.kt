package com.undef.superahorro.caparrozruiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.undef.superahorro.caparrozruiz.R

@Composable
fun EditProductCard(
    name: String,
    description: String,
    quantity: Int,
    price: Double,
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Double) -> Unit
) {
    var editedName by remember { mutableStateOf(name) }
    var editedDescription by remember { mutableStateOf(description) }
    var editedQuantity by remember { mutableStateOf(quantity.toString()) }
    var editedPrice by remember { mutableStateOf(price.toString()) }

    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = stringResource(R.string.purchase_detail_edit_product_title), style = MaterialTheme.typography.titleMedium)
            AppTextField(
                value = editedName,
                onValueChange = { editedName = it },
                label = stringResource(R.string.product_new_name_label)
            )
            AppTextField(
                value = editedDescription,
                onValueChange = { editedDescription = it },
                label = stringResource(R.string.product_new_description_label),
                singleLine = false
            )
            AppTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                label = stringResource(R.string.product_new_quantity_label),
                keyboardType = KeyboardType.Number
            )
            AppTextField(
                value = editedPrice,
                onValueChange = { editedPrice = it },
                label = stringResource(R.string.product_new_price_label),
                keyboardType = KeyboardType.Number
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.common_cancel))
                }
                OutlinedButton(
                    onClick = {
                        val quantityValue = editedQuantity.toIntOrNull() ?: 0
                        val priceValue = editedPrice.toDoubleOrNull() ?: 0.0
                        onSave(editedName, editedDescription, quantityValue, priceValue)
                    }
                ) {
                    Text(text = stringResource(R.string.common_save))
                }
            }
        }
    }
}
