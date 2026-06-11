package com.undef.superahorro.caparrozruiz.ui.screens.promotions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.undef.superahorro.caparrozruiz.ui.viewmodel.PromotionsViewModel

@Composable
fun PromotionsScreen(viewModel: PromotionsViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.promotions_title), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.promotions_subtitle), style = MaterialTheme.typography.bodyMedium)

        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
                Text(text = stringResource(R.string.promotions_loading))
            }
            uiState.errorMessage != null -> {
                Text(text = stringResource(R.string.promotions_error))
                Text(text = uiState.errorMessage.orEmpty(), style = MaterialTheme.typography.bodySmall)
                Button(onClick = viewModel::refreshFromNetwork) {
                    Text(text = stringResource(R.string.promotions_retry))
                }
            }
            uiState.promotions.isEmpty() -> {
                Text(text = stringResource(R.string.promotions_empty))
                Button(onClick = viewModel::refreshFromNetwork) {
                    Text(text = stringResource(R.string.promotions_refresh))
                }
            }
            else -> {
                Button(onClick = viewModel::refreshFromNetwork) {
                    Text(text = stringResource(R.string.promotions_refresh))
                }
                if (uiState.refreshError != null) {
                    Text(
                        text = uiState.refreshError.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.promotions, key = { it.id }) { promotion ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(text = promotion.title, style = MaterialTheme.typography.titleMedium)
                                Text(text = promotion.description, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
