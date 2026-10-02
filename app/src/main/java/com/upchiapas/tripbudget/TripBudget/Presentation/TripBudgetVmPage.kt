package com.upchiapas.tripbudget.TripBudget.Presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TripBudgetVmPage(viewModel: TripBudgetViewModel = viewModel()) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "✈️ TripBudget",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Datos del viaje",
            style = MaterialTheme.typography.titleMedium
        )

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

        Text(text = "Gasto total: ${state.totalCostText}")
        Text(text = "${state.differenceLabel}: ${state.differenceText}")
        Text(text = "Presupuesto utilizado: ${state.percentText}")
        Text(text = state.statusMessage)
    }
}

// Composable reutilizable que avisa cuando cambia
@Composable
fun BudgetInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        prefix = { Text("\$") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier.fillMaxWidth()
    )
}

// Composable reutilizable de etiqueta + Slider.
@Composable
fun LabeledSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            // steps = valores intermedios, para que el slider salte de 1 en 1
            steps = (valueRange.endInclusive - valueRange.start).toInt() - 1
        )
    }
}