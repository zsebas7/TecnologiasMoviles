package com.undef.superahorro.caparrozruiz.ui.screens.purchase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.components.AppTextField
import com.undef.superahorro.caparrozruiz.ui.components.PrimaryButton
import com.undef.superahorro.caparrozruiz.ui.viewmodel.NewPurchaseViewModel

@Composable
fun NewProductScreen(
    onProductAdded: () -> Unit,
    viewModel: NewPurchaseViewModel = viewModel()
) {
    val uiState by viewModel.productState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.product_new_title), style = MaterialTheme.typography.headlineMedium)
        AppTextField(
            value = uiState.name,
            onValueChange = viewModel::onProductNameChanged,
            label = stringResource(R.string.product_new_name_label)
        )
        AppTextField(
            value = uiState.quantity,
            onValueChange = viewModel::onProductQuantityChanged,
            label = stringResource(R.string.product_new_quantity_label)
        )
        AppTextField(
            value = uiState.price,
            onValueChange = viewModel::onProductPriceChanged,
            label = stringResource(R.string.product_new_price_label)
        )
        Spacer(modifier = Modifier.height(6.dp))
        PrimaryButton(
            text = stringResource(R.string.product_new_add_button),
            loading = false,
            onClick = { viewModel.addProduct(onProductAdded) }
        )
    }
}
