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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.undef.superahorro.caparrozruiz.R

@Composable
fun EditProductCard(
    code: String,
    name: String,
    description: String,
    quantity: Int,
    price: Double,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Int, Double) -> Unit
) {
    var editedCode by remember { mutableStateOf(code) }
    var editedName by remember { mutableStateOf(name) }
    var editedDescription by remember { mutableStateOf(description) }
    var editedQuantity by remember { mutableStateOf(quantity.toString()) }
    var editedPrice by remember { mutableStateOf(price.toString()) }

    Card {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = stringResource(R.string.purchase_detail_edit_product_title), style = MaterialTheme.typography.titleMedium)
            AppTextField(
                value = editedCode,
                onValueChange = { editedCode = it },
                label = stringResource(R.string.product_new_code_label)
            )
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
                label = stringResource(R.string.product_new_quantity_label)
            )
            AppTextField(
                value = editedPrice,
                onValueChange = { editedPrice = it },
                label = stringResource(R.string.product_new_price_label)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.common_cancel))
                }
                OutlinedButton(
                    onClick = {
                        val quantityValue = editedQuantity.toIntOrNull() ?: 0
                        val priceValue = editedPrice.toDoubleOrNull() ?: 0.0
                        onSave(editedCode, editedName, editedDescription, quantityValue, priceValue)
                    }
                ) {
                    Text(text = stringResource(R.string.common_save))
                }
            }
        }
    }
}
