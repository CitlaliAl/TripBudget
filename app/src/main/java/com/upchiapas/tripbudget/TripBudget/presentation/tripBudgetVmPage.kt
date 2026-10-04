package com.upchiapas.tripbudget.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upchiapas.tripbudget.presentation.components.BreakdownCard
import com.upchiapas.tripbudget.presentation.components.BudgetInputField
import com.upchiapas.tripbudget.presentation.components.BudgetProgress
import com.upchiapas.tripbudget.presentation.components.BudgetStatusCard
import com.upchiapas.tripbudget.presentation.components.LabeledSlider
import com.upchiapas.tripbudget.presentation.components.TripSummaryCard
import com.upchiapas.tripbudget.presentation.components.TripBudgetLogo

@Composable
fun TripBudgetVmPage(viewModel: TripBudgetViewModel = viewModel()) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val statusColor = statusColor(state.status)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        TripBudgetLogo (modifier = Modifier.align(Alignment.CenterHorizontally)) // Logo de la app

        // Datos del viaje
        Text(text = "Datos del viaje", style = MaterialTheme.typography.titleMedium)

        BudgetInputField(
            label = "Presupuesto disponible",
            value = state.budgetInput,
            onValueChange = viewModel::onBudgetChange
        )

        LabeledSlider(
            label = "Personas: ${state.people}",
            value = state.people.toFloat(),
            valueRange = TripBudgetLimits.MIN_PEOPLE.toFloat()..TripBudgetLimits.MAX_PEOPLE.toFloat(),
            onValueChange = viewModel::onPeopleChange
        )

        LabeledSlider(
            label = "Días: ${state.days}",
            value = state.days.toFloat(),
            valueRange = TripBudgetLimits.MIN_DAYS.toFloat()..TripBudgetLimits.MAX_DAYS.toFloat(),
            onValueChange = viewModel::onDaysChange
        )

        BudgetInputField(
            label = "Hospedaje por noche",
            value = state.lodgingInput,
            onValueChange = viewModel::onLodgingChange
        )

        BudgetInputField(
            label = "Comida por persona al día",
            value = state.foodInput,
            onValueChange = viewModel::onFoodChange
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Incluir transporte", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = state.includeTransport,
                onCheckedChange = viewModel::onIncludeTransportChange
            )
        }

        if (state.includeTransport) {
            BudgetInputField(
                label = "Costo del transporte",
                value = state.transportInput,
                onValueChange = viewModel::onTransportChange
            )
        }

        HorizontalDivider()

        // Resultados
        Text(text = "Resultados", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TripSummaryCard(
                title = "Gasto total",
                value = state.totalCostText,
                modifier = Modifier.weight(1f)
            )
            TripSummaryCard(
                title = state.differenceLabel,
                value = state.differenceText,
                accentColor = statusColor,
                modifier = Modifier.weight(1f)
            )
        }

        BudgetProgress(
            progress = state.progress,
            percentText = state.percentText,
            color = statusColor
        )

        BreakdownCard(
            lodging = state.lodgingText,
            food = state.foodText,
            transport = state.transportText
        )

        BudgetStatusCard(
            message = state.statusMessage,
            icon = statusIcon(state.status),
            color = statusColor
        )
    }
}