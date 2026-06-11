package com.undef.superahorro.caparrozruiz.ui.screens.purchase

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.components.AppTextField
import com.undef.superahorro.caparrozruiz.ui.components.PrimaryButton
import com.undef.superahorro.caparrozruiz.ui.viewmodel.NewPurchaseViewModel
import java.io.File

@Composable
fun NewPurchaseScreen(
    onAddProduct: () -> Unit,
    onSaved: () -> Unit,
    viewModel: NewPurchaseViewModel = viewModel()
) {
    val uiState by viewModel.purchaseState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.setTicketUri(it.toString()) }
    }

    val pendingCameraUri = remember { mutableStateOf<android.net.Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) pendingCameraUri.value?.let { viewModel.setTicketUri(it.toString()) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) pendingCameraUri.value?.let { cameraLauncher.launch(it) }
    }

    val launchCamera = {
        runCatching {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile("ticket_", ".jpg", dir)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            pendingCameraUri.value = uri
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                    onValueChange = viewModel::onTotalChanged,
                    label = stringResource(R.string.purchase_new_total_label),
                    keyboardType = KeyboardType.Number
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = stringResource(R.string.purchase_new_ticket_title), style = MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                            Text(text = stringResource(R.string.purchase_new_ticket_gallery))
                        }
                        OutlinedButton(onClick = { launchCamera() }) {
                            Text(text = stringResource(R.string.purchase_new_ticket_camera))
                        }
                    }
                    if (uiState.ticketUri.isNotBlank()) {
                        Text(
                            text = stringResource(R.string.purchase_new_ticket_attached),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        AsyncImage(
                            model = uiState.ticketUri,
                            contentDescription = stringResource(R.string.purchase_new_ticket_title),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentScale = ContentScale.Crop
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

        uiState.products.forEach { product ->
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

        PrimaryButton(
            text = stringResource(R.string.purchase_new_save_button),
            loading = uiState.isSaving,
            onClick = { viewModel.savePurchase(onSaved) }
        )
    }
}
