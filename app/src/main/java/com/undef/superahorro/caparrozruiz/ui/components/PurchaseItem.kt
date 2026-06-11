package com.undef.superahorro.caparrozruiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.undef.superahorro.caparrozruiz.data.model.Purchase
import com.undef.superahorro.caparrozruiz.util.toCurrencyString

@Composable
fun PurchaseItem(
    purchase: Purchase,
    currencyLabel: String,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locales[0]
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = purchase.market, style = MaterialTheme.typography.titleMedium)
                Text(text = purchase.date, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "$currencyLabel ${purchase.total.toCurrencyString(locale)}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
