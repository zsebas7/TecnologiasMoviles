package com.undef.superahorro.caparrozruiz.ui.screens.settings

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onLogoutClick: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(text = stringResource(R.string.settings_title), style = MaterialTheme.typography.headlineMedium)
        Card {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.settings_notifications))
                    Switch(
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = viewModel::setNotificationsEnabled
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.settings_monthly_summary))
                    Switch(
                        checked = uiState.monthlySummaryEnabled,
                        onCheckedChange = viewModel::setMonthlySummaryEnabled
                    )
                }
            }
        }
        Button(
            onClick = {
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("soporte@superahorro.com"))
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_email_subject))
                }
                val chooser = Intent.createChooser(emailIntent, context.getString(R.string.settings_email_chooser))
                startActivity(context, chooser, null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings_email_button))
        }
        Button(
            onClick = viewModel::syncPurchases,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSyncing
        ) {
            Text(text = if (uiState.isSyncing) stringResource(R.string.settings_syncing_button) else stringResource(R.string.settings_sync_button))
        }
        if (uiState.syncMessage.isNotBlank()) {
            Text(text = uiState.syncMessage, style = MaterialTheme.typography.bodySmall)
        }
        if (uiState.syncError.isNotBlank()) {
            Text(text = uiState.syncError, style = MaterialTheme.typography.bodySmall)
        }
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings_logout_button))
        }
    }
}
