package com.undef.superahorro.caparrozruiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.data.model.Product

@Composable
fun DraftProductItem(product: Product, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = product.name, style = MaterialTheme.typography.titleSmall)
            Text(text = product.description, style = MaterialTheme.typography.bodySmall)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.purchase_new_product_quantity, product.quantity))
                Text(text = stringResource(R.string.purchase_new_product_price, product.price))
            }
            Button(onClick = onRemove) {
                Text(text = stringResource(R.string.purchase_new_remove_product_button))
            }
        }
    }
}
