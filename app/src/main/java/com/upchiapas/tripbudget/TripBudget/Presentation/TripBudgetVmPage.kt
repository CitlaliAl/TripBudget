package com.upchiapas.tripbudget.TripBudget.Presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TripBudgetVmPage(viewModel: TripBudgetViewModel = viewModel()) {
    // Convierte el StateFlow en un State de Compose. Aquí es donde recompone.
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "✈️ TripBudget",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = state.statusMessage,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Gasto total: ${state.totalCostText}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
