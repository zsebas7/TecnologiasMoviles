package com.undef.superahorro.caparrozruiz.ui.screens.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.undef.superahorro.caparrozruiz.R

@Composable
fun MoreScreen(
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPromotions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.more_title), style = MaterialTheme.typography.headlineMedium)
        MoreItem(
            onClick = onOpenProfile,
            title = stringResource(R.string.more_profile_title),
            subtitle = stringResource(R.string.more_profile_subtitle)
        )
        MoreItem(
            title = stringResource(R.string.more_settings_title),
            subtitle = stringResource(R.string.more_settings_subtitle),
            onClick = onOpenSettings
        )
        MoreItem(
            title = stringResource(R.string.more_promotions_title),
            subtitle = stringResource(R.string.more_promotions_subtitle),
            onClick = onOpenPromotions
        )
    }
}

@Composable
private fun MoreItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
            }
            Text(text = stringResource(R.string.more_open_action), style = MaterialTheme.typography.labelLarge)
        }
    }
}
