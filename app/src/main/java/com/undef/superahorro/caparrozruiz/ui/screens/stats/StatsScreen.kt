package com.undef.superahorro.caparrozruiz.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.viewmodel.StatsMode
import com.undef.superahorro.caparrozruiz.ui.viewmodel.StatsViewModel
import androidx.compose.foundation.Canvas

@Composable
fun StatsScreen(viewModel: StatsViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val maxValue = uiState.entries.maxOfOrNull { it.value } ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.stats_title), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.stats_subtitle), style = MaterialTheme.typography.bodyMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.mode == StatsMode.Daily) {
                Button(onClick = { viewModel.setMode(StatsMode.Daily) }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.stats_mode_daily))
                }
                OutlinedButton(onClick = { viewModel.setMode(StatsMode.Monthly) }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.stats_mode_monthly))
                }
            } else {
                OutlinedButton(onClick = { viewModel.setMode(StatsMode.Daily) }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.stats_mode_daily))
                }
                Button(onClick = { viewModel.setMode(StatsMode.Monthly) }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.stats_mode_monthly))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = stringResource(R.string.stats_chart_title), style = MaterialTheme.typography.titleMedium)
                if (uiState.entries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                color = Color.LightGray.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(text = stringResource(R.string.stats_chart_empty))
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        uiState.entries.forEach { entry ->
                            val ratio = if (maxValue > 0) (entry.value / maxValue).toFloat() else 0f
                            val barHeight = (ratio * 140f).coerceAtLeast(12f)
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = stringResource(R.string.stats_chart_value, entry.value),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(barHeight.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = entry.label, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = stringResource(R.string.stats_pie_title), style = MaterialTheme.typography.titleMedium)
                if (uiState.categories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                color = Color.LightGray.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(text = stringResource(R.string.stats_pie_empty))
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Canvas(modifier = Modifier.size(160.dp)) {
                            val total = uiState.categories.sumOf { it.value }
                            var startAngle = -90f
                            uiState.categories.forEach { slice ->
                                val sweep = if (total > 0) (slice.value / total * 360f).toFloat() else 0f
                                drawArc(
                                    color = slice.color,
                                    startAngle = startAngle,
                                    sweepAngle = sweep,
                                    useCenter = true
                                )
                                startAngle += sweep
                            }
                            drawCircle(
                                color = Color.White,
                                radius = size.minDimension * 0.25f,
                                style = Stroke(width = 24f)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.categories.forEach { slice ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(slice.color, shape = RoundedCornerShape(2.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(
                                            R.string.stats_pie_item,
                                            slice.label,
                                            slice.value
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
